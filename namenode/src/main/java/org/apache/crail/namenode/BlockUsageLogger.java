package org.apache.crail.namenode;

public class BlockUsageLogger implements Runnable {
    
    NameNodeService service;

    BlockUsageLogger(NameNodeService service) {
        this.service = service;
        Thread runner = new Thread(this);
        runner.start();
    }

    public void run() {
        while(true) {

            try {
                System.out.println("Current block usage: " + this.service.getBlockUsage());
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
