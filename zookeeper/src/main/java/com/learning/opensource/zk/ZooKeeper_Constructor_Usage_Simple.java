package com.learning.opensource.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 */
public class ZooKeeper_Constructor_Usage_Simple implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);

    public static void main(String[] args) throws  Exception{
        ZooKeeper zookeeper=new ZooKeeper("dolphin.zk.1:2181,dolphin.zk.2:2182,dolphin.zk.3:2181",5000,new ZooKeeper_Constructor_Usage_Simple());

        System.out.println(zookeeper.getState());
        try{
            connectedSemaphore.await();
        }catch (InterruptedException e){                                                                                                      }
        System.out.println("Zookeeper session established.");
    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event "+watchedEvent);
        if(Event.KeeperState.SyncConnected== watchedEvent.getState()){
            connectedSemaphore.countDown();
        }
    }
}
