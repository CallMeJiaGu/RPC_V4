package main.java.ZKUtil;

import org.apache.zookeeper.*;

import java.util.List;

/**
 * Created by GEKL on 2018/9/5.
 */
public class ZKUtils {

    private static String zkConnectStr = "127.0.0.1:2181";
    private static int sessionTimeOut = 50000;

    public static ZooKeeper getConnnet() throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(zkConnectStr, sessionTimeOut, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //
            }
        });
        return zooKeeper;
    }

    /**
     * 创建临时节点
     * @param zk 链接成功zk实例
     * @param serviceRootName 注册的服务名称
     * @param childName 注册在该名称下的服务节点的IP地址
     * @return 返回创建成功的节点路径
     * @throws Exception
     */
    public static String createEphemNode(ZooKeeper zk,String serviceRootName,String childName,int port) throws Exception{
        return zk.create("/"+serviceRootName+"/"+childName+","+port,childName.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    /**
     * 根据服务名返回所有注册在上的IP
     * @param zk 实例化的zk
     * @param serviceRootName 服务名称
     * @return 返回可以使用的IPs
     * @throws Exception
     */
    public static List<String> getChildren(ZooKeeper zk,String serviceRootName) throws Exception{
        return zk.getChildren("/"+serviceRootName,false);
    }
}
