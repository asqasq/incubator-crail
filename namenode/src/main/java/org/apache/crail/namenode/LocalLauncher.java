package org.apache.crail.namenode;

import org.apache.crail.utils.CrailUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LocalLauncher implements  DatanodeLauncher {

    static final Logger LOG = CrailUtils.getLogger();
    int instances = 0;

    LocalLauncher() {

    }

    @Override
    public void launchTCPinstance() {

        try {
            this.instances++;
            String port = Integer.toString(50020+this.instances);
            Process p = new ProcessBuilder(System.getenv("CRAIL_HOME") + "/bin/crail", "datanode", "--",  "-p" + port).start();
            //Process p = Runtime.getRuntime().exec("pwd");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            System.out.println("Here is the standard output of the command:\n");
            String s = null;

            System.out.println("Here is the standard error of the command (if any):\n");
            //while ((s = errorReader.readLine()) != null) {
            //    System.out.println(s);
            //}

            //while ((s = inputReader.readLine()) != null) {
            //    System.out.println(s);
            //}


            LOG.info("Launched new TCP instance");
        } catch(Exception e) {
            System.out.println("Unable to launch local Datanode");
            e.printStackTrace();
        }

    }
}
