package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 *
 * 打印结果：
    Received watched event WatchedEvent state:SyncConnected type:None path:null
    Create path result:[0,/zk-test-persisitent3-,I am context., real path name: /zk-test-persisitent3-
    Create path result:[0,/zk-test-ephemeral3-,I am context., real path name: /zk-test-ephemeral3-0000000011
 * 异步创建节点
 */
public class ZooKeeper_Create_API_Asy_Usage implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);

    public static void main(String[] args) throws  Exception{
        ZooKeeper zookeeper=new ZooKeeper("10.155.20.116:2181",5000,new ZooKeeper_Create_API_Asy_Usage());

        connectedSemaphore.await();
        //OPEN_ACL_UNSAFE 表示权限不受控制
        zookeeper.create("/zk-test-persisitent3-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,new IStringCallback(),"I am context.");

        zookeeper.create("/zk-test-ephemeral3-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallback(),"I am context.");

        Thread.sleep(Integer.MAX_VALUE);

    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event "+watchedEvent);
        if(Event.KeeperState.SyncConnected== watchedEvent.getState()){
            connectedSemaphore.countDown();
        }
    }

}
class IStringCallback implements AsyncCallback.StringCallback{
    public void processResult(int i, String s, Object o, String s1) {
        //i是ResultCode,服务端的响应码，客户端根据这个响应码去识别结果： 0(成功),-4(客户端和服务端连接已经断了),-110(指定的节点已经存在),-112(回话过期)
        System.out.println("Create path result:["+i+","+s+","+o+", real path name: "+s1);
    }
}