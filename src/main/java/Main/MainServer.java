package main.java.Main;

import main.java.MyServer.HelloService;
import main.java.MyServer.HelloServiceImpl;
import main.java.Server.RPCServer;
import main.java.Server.ServerHandler;
import main.java.ZKUtil.ZKUtils;
import org.apache.zookeeper.ZooKeeper;

import java.net.InetAddress;

/**
 * Created by GEKL on 2018/9/5.
 */
public class MainServer {

    public static void main(String[] args) throws Exception {
        int port = 8084;
        if(args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e) {
                //采用默认值
            }
        }
        ServerHandler serverHandler = new ServerHandler();
        serverHandler.Register(HelloService.class.getName(), HelloServiceImpl.class);
        //把服务注册到Zookeeper上
        ServerRegister(InetAddress.getLocalHost().getHostAddress(),port);
        new RPCServer(serverHandler).bind(port);
    }

    public static void  ServerRegister(String ipAddress ,int port) throws Exception{
        System.out.println("服务在"+ipAddress+":"+port+"上运行");
        System.out.println("和ZK建立成功");
        ZooKeeper zk = ZKUtils.getConnnet();
        System.out.println("在ZK建立临时节点成功");
        ZKUtils.createEphemNode(zk,"HelloService",ipAddress,port);
    }

}
