package org.apache.crail.namenode;

import org.apache.crail.metadata.DataNodeInfo;
import org.apache.crail.rpc.RpcNameNodeService;
import org.apache.crail.utils.CrailUtils;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class RemoveDataNodeTrigger implements Runnable {

    private static final Logger LOG = CrailUtils.getLogger();
    NameNodeService service;

    RemoveDataNodeTrigger(RpcNameNodeService service) {
        this.service = (NameNodeService) service;
        Thread runner = new Thread(this);
        runner.start();
    }

    public void run() {

        LOG.info("Remove DataNodeTrigger is running");

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));

            while(true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                ByteBuffer buff = ByteBuffer.allocate(8);
                int bytesRead = socketChannel.read(buff);
                socketChannel.close();

                byte[] ipaddr = ByteBuffer.allocate(4).putInt(buff.getInt(0)).array();
                int port = buff.getInt(4);

                LOG.info("Received trigger to remove DataNode at: " + InetAddress.getByAddress(ipaddr).getHostAddress() + ":" + port);

                DataNodeInfo dn = new DataNodeInfo(0, 0, 0, ipaddr, port);
                service.prepareDataNodeForRemoval(dn);
            }
        } catch (Exception e) {
            LOG.info("Unable to create additional socket to trigger the removal of DataNodes");
        }
    }
}
