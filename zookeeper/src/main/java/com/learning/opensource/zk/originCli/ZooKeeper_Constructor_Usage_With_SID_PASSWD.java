package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 *
 * 打印结果：
 * Received watched event WatchedEvent state:SyncConnected type:None path:null
 * Received watched event WatchedEvent state:Expired type:None path:null
 * Received watched event WatchedEvent state:SyncConnected type:None path:null
 * 第一次使用了错误的sessionId和 SessionPasswd创建Zookeeper客户端实例，结果客户端
 * 收到了服务端的Expired事件通知；第二次用了正确的sessionId和 SessionPasswd创建Zookeeper客户端实例，
 * 结果连接成功
 */
public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);

    public static void main(String[] args) throws  Exception{
        ZooKeeper zookeeper=new ZooKeeper("dolphin.zk.1:2181,dolphin.zk.2:2182,dolphin.zk.3:2181",5000,new ZooKeeper_Constructor_Usage_With_SID_PASSWD());
        try{
            connectedSemaphore.await();
        }catch (InterruptedException e){
        }
        long sessionId=zookeeper.getSessionId();
        byte[] passwd=zookeeper.getSessionPasswd();
        zookeeper=new ZooKeeper("dolphin.zk.1:2181,dolphin.zk.2:2182,dolphin.zk.3:2181",5000,new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),1l,"test".getBytes());

        zookeeper=new ZooKeeper("dolphin.zk.1:2181,dolphin.zk.2:2182,dolphin.zk.3:2181",5000,new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),sessionId,passwd);

        Thread.sleep(Integer.MAX_VALUE);

    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event "+watchedEvent);
        if(Event.KeeperState.SyncConnected== watchedEvent.getState()){
            connectedSemaphore.countDown();
        }
    }
}
