package org.apache.crail.namenode;

import org.apache.crail.utils.CrailUtils;
import org.slf4j.Logger;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.util.Yaml;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KubernetesClient {

    static final Logger LOG = CrailUtils.getLogger();
    BatchV1Api apiInstance;
    int tcpInstances = 0;

    KubernetesClient() {

        try {
            LOG.info("Trying to create defaultClient");
            ApiClient client = ClientBuilder.defaultClient();
            LOG.info("Created defaultClient");
            Configuration.setDefaultApiClient(client);
            LOG.info("Set defaultClient as APiClient");
            apiInstance = new BatchV1Api(client);
            LOG.info("Instantiate API");
        } catch(Exception e) {
            System.err.println("Unable to create default Kubernetes API client");
        }
        
    }

    public void launchTCPinstance() {

        V1Job body = null;

        try {
            this.tcpInstances++;
            String base = System.getenv("CRAIL_HOME");
            byte[] encoded = Files.readAllBytes(Paths.get(base+"/conf/datanode-dram-job.yaml"));
            String input = new String(encoded, StandardCharsets.UTF_8);
            input = input.replace("myjob", "datanode-tcp-"+this.tcpInstances);
            body = Yaml.loadAs(input, V1Job.class);
            
        } catch(Exception e) {
            System.err.println("Unable to load yaml file at: " + (System.getenv("CRAIL_HOME")+"/conf/datanode-dram-job.yaml"));
        }


        try {
            V1Job resp = apiInstance.createNamespacedJob("crail", body, null, null, null);
            LOG.info("Launched new TCP datanode");
          } catch(ApiException e) {
              LOG.error("Exception when calling BatchV1Api#createNamespacedJob");
              System.err.println("Status code: " + e.getCode());
              System.err.println("Reason: " + e.getResponseBody());
              System.err.println("Response headers: " + e.getResponseHeaders());
              e.printStackTrace();
          }

    }

}