package org.apache.crail.namenode;

import org.apache.crail.utils.CrailUtils;
import org.slf4j.Logger;

public class BlockUsageLogger implements Runnable {
    
    NameNodeService service;
    static final Logger LOG = CrailUtils.getLogger();

    BlockUsageLogger(NameNodeService service) {
        this.service = service;
        Thread runner = new Thread(this);
        runner.start();
    }

    public void run() {
        while(true) {

            try {
                LOG.info("Current block usage:    " + this.service.getBlockUsage() + "/" + this.service.getBlockCapacity());
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
