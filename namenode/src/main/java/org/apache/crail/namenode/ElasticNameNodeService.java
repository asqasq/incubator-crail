package org.apache.crail.namenode;

import java.io.IOException;

import org.apache.crail.conf.CrailConstants;

public class ElasticNameNodeService extends NameNodeService {
    

    DatanodeLauncher datanodeLauncher;

    PolicyRunner policyRunner;

    //TODO: allow for different policies and configuration of parameters
    BlockUsageLogger blockUsageLogger = new BlockUsageLogger(this);


    public ElasticNameNodeService() throws IOException {

        if(CrailConstants.ELASTICSTORE_DATANODE_LAUNCHER.equalsIgnoreCase("local")) {
            this.datanodeLauncher = new LocalLauncher();
        } else if(CrailConstants.ELASTICSTORE_DATANODE_LAUNCHER.equalsIgnoreCase("kubernetes")) {
            this.datanodeLauncher = new KubernetesClient();
        } else {
            System.err.println("Unknown DatanodeLauncher " + CrailConstants.ELASTICSTORE_DATANODE_LAUNCHER);
        }



        this.policyRunner = new FreeCapacityPolicy(this, datanodeLauncher, CrailConstants.ELASTICSTORE_SCALEUP, CrailConstants.ELASTICSTORE_SCALEDOWN,
                CrailConstants.ELASTICSTORE_MINNODES, CrailConstants.ELASTICSTORE_MAXNODES);
    }
}
