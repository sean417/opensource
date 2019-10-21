package com.learning.opensource.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 */
public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);

    public static void main(String[] args) throws  Exception{
        ZooKeeper zookeeper=new ZooKeeper("dolphin.zk.1:2181,dolphin.zk.2:2182,dolphin.zk.3:2181",5000,new ZooKeeper_Constructor_Usage_With_SID_PASSWD());
        long sessionId=zookeeper.getSessionId();
        byte[] passwd=zookeeper.getSessionPasswd();
        connectedSemaphore.await();
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
