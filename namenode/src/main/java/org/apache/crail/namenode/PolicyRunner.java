package org.apache.crail.namenode;

import org.apache.crail.conf.CrailConstants;
import org.apache.crail.metadata.DataNodeInfo;
import org.apache.crail.rpc.RpcNameNodeService;
import org.apache.crail.utils.CrailUtils;
import org.slf4j.Logger;

public abstract class PolicyRunner implements Runnable {

    static final Logger LOG = CrailUtils.getLogger();
    NameNodeService service;
    DatanodeLauncher datanodeLauncher;

    PolicyRunner(RpcNameNodeService service, DatanodeLauncher datanodeLauncher){
        this.service = (NameNodeService) service;
        Thread runner = new Thread(this);
        this.datanodeLauncher = datanodeLauncher;
        runner.start();
    }

    public abstract void checkPolicy();

    public void run() {

        while(true) {
            checkPolicy();

            try {
                Thread.sleep(CrailConstants.ELASTICSTORE_POLICYRUNNER_INTERVAL * 1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}
