package org.apache.crail.namenode;

import org.apache.crail.metadata.DataNodeInfo;
import org.apache.crail.rpc.RpcNameNodeService;
import org.apache.crail.utils.CrailUtils;
import org.slf4j.Logger;

public abstract class PolicyRunner implements Runnable {

    static final Logger LOG = CrailUtils.getLogger();
    NameNodeService service;
    KubernetesClient kubernetesClient;

    PolicyRunner(RpcNameNodeService service, KubernetesClient kubernetesClient){
        this.service = (NameNodeService) service;
        Thread runner = new Thread(this);
        this.kubernetesClient = kubernetesClient;
        runner.start();
    }

    public abstract void checkPolicy();

    public void run() {

        while(true) {
            checkPolicy();

            try {
                Thread.sleep(10000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}
