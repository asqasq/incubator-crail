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
    int port;

    RemoveDataNodeTrigger(RpcNameNodeService service) {
        this.service = (NameNodeService) service;
        this.port = 9999;
        Thread runner = new Thread(this);
        runner.start();
    }

    public void run() {

        LOG.info("RemoveDataNodeTrigger is running on port " + this.port);

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(this.port));

            while(true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                ByteBuffer buff = ByteBuffer.allocate(8);

                int bytesRead = socketChannel.read(buff);
                socketChannel.close();

                if(bytesRead != 8){
                    LOG.info("Expected to receive 8 bytes (IP-Addr,Portnumber)");
                    buff.clear();
                    continue;
                }

                buff.flip();

                // Reading input: (IPv4-Addr: 4-bytes, Portnumber: 4-bytes)
                byte[] ipaddr = ByteBuffer.allocate(4).putInt(buff.getInt(0)).array();
                int port = buff.getInt(4);

                buff.clear();

                LOG.info("Received trigger to remove DataNode at: " + InetAddress.getByAddress(ipaddr).getHostAddress() + ":" + port);

                DataNodeInfo dn = new DataNodeInfo(0, 0, 0, ipaddr, port);
                service.prepareDataNodeForRemoval(dn);
            }
        } catch (Exception e) {
            LOG.info("Unable to create additional socket to trigger the removal of DataNodes");
        }
    }
}
