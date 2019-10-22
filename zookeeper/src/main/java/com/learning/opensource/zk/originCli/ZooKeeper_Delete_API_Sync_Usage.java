package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 *
 * 打印结果：
 *  Received watched event WatchedEvent state:SyncConnected type:None path:null
 *  Success create znode:/zk-test-ephemeral1-
 * Success create znode:/zk-test-ephemeral1-0000000007
 * 同步删除节点
 */
public class ZooKeeper_Delete_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);

    public static void main(String[] args) throws  Exception{
        ZooKeeper zookeeper=new ZooKeeper("10.155.20.155:2181",5000,new ZooKeeper_Delete_API_Sync_Usage());

        connectedSemaphore.await();
        //
        zookeeper.delete("/zk-test-ephemeral1-",0);


        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event "+watchedEvent);
        if(Event.KeeperState.SyncConnected== watchedEvent.getState()){
            connectedSemaphore.countDown();
        }
    }
}
