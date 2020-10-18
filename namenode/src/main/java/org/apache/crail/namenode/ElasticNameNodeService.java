package org.apache.crail.namenode;

import java.io.IOException;

import org.apache.crail.conf.CrailConstants;

public class ElasticNameNodeService extends NameNodeService {
    

    KubernetesClient kubernetesClient = new KubernetesClient();
    
    //TODO: allow for different policies and configuration of parameters
    PolicyRunner policyRunner = new FreeCapacityPolicy(this, kubernetesClient, CrailConstants.ELASTICSTORE_SCALEUP, CrailConstants.ELASTICSTORE_SCALEDOWN,
                                                       CrailConstants.ELASTICSTORE_MINNODES, CrailConstants.ELASTICSTORE_MAXNODES);

    public ElasticNameNodeService() throws IOException {

    }
}
