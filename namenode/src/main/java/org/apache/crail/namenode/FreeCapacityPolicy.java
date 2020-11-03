package org.apache.crail.namenode;

import org.apache.crail.metadata.DataNodeInfo;
import org.apache.crail.rpc.RpcNameNodeService;

public class FreeCapacityPolicy extends PolicyRunner {

    double scaleUp;
    double scaleDown;
    int minDataNodes;
    int maxDataNodes;
    int datanodes; // maintains the desired number of datanodes (i.e. a datanode might be still starting / terminating)

    FreeCapacityPolicy(RpcNameNodeService service, DatanodeLauncher datanodeLauncher, double scaleUp, double scaleDown, int minDataNodes, int maxDataNodes) {
        super(service, datanodeLauncher);
        this.scaleUp = scaleUp;
        this.scaleDown = scaleDown;
        this.minDataNodes = minDataNodes;
        this.maxDataNodes = maxDataNodes;
        this.datanodes = 0;
    }

    @Override
    public void checkPolicy() {

        try {
            double usage = this.service.getStorageUsage();
            LOG.info("Current storage usage is " + 100*usage + "%.");
            LOG.info("Current number of datanodes is " + this.datanodes + ".");

            if(usage < scaleDown && this.datanodes > minDataNodes) {
                LOG.info("Scale down detected");

                DataNodeBlocks removeCandidate = this.service.identifyRemoveCandidate();
                this.service.prepareDataNodeForRemoval(removeCandidate);
                this.datanodes--;
            }

            if(usage > this.scaleUp && this.datanodes < maxDataNodes) {
                LOG.info("Scale up detected");
                datanodeLauncher.launchTCPinstance();
                this.datanodes++;
            }


        } catch (Exception e) {
            LOG.error("Unable to retrieve storage usage information");
        }


    }
}