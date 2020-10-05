package org.apache.crail.namenode;

import java.io.IOException;

public class ElasticNameNodeService extends NameNodeService {
    

    KubernetesClient kubernetesClient = new KubernetesClient();
    
    //TODO: allow for different policies and configuration of parameters
    PolicyRunner policyRunner = new FreeCapacityPolicy(this, kubernetesClient, 0.1, 0.05, 1, 10);

    public ElasticNameNodeService() throws IOException {

    }
}
