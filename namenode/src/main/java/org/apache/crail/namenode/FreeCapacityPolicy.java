package org.apache.crail.namenode;

import org.apache.crail.metadata.DataNodeInfo;
import org.apache.crail.rpc.RpcNameNodeService;

public class FreeCapacityPolicy extends PolicyRunner {

    double scaleUp;
    double scaleDown;
    int minDataNodes;
    int maxDataNodes;

    FreeCapacityPolicy(RpcNameNodeService service, KubernetesClient kubernetesClient, double scaleUp, double scaleDown, int minDataNodes, int maxDataNodes) {
        super(service, kubernetesClient);
        this.scaleUp = scaleUp;
        this.scaleDown = scaleDown;
        this.minDataNodes = minDataNodes;
        this.maxDataNodes = maxDataNodes;
    }

    @Override
    public void checkPolicy() {

        try {
            double usage = this.service.getStorageUsage();
            LOG.info("Current storage usage is " + 100*usage + "%");

            if(usage < scaleDown && this.service.getNumberDatanodes() > minDataNodes) {
                LOG.info("Scale down detected");

                DataNodeBlocks removeCandidate = this.service.identifyRemoveCandidate();
                this.service.prepareDataNodeForRemoval(removeCandidate);
            }

            if(usage > this.scaleUp && this.service.getNumberDatanodes() < maxDataNodes) {
                LOG.info("Scale up detected");
                kubernetesClient.launchTCPinstance();
            }


        } catch (Exception e) {
            LOG.error("Unable to retrieve storage usage information");
        }


    }
}