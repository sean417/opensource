package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 *
 * 打印结果：节点数据内容或节点版本变化了，都视为zookeeper节点的变化。
Received watched event WatchedEvent state:SyncConnected type:None path:null
Received watched event WatchedEvent state:SyncConnected type:NodeCreated path:/zk-book-exist
Node(/zk-book-exist)Created
Received watched event WatchedEvent state:SyncConnected type:NodeDataChanged path:/zk-book-exist
Node(/zk-book-exist)DataChanged
Received watched event WatchedEvent state:SyncConnected type:NodeDeleted path:/zk-book-exist
Node(/zk-book-exist)Deleted

 */
public class Exist_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zookeeper = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception {
        String path = "/zk-book-exist";
        zookeeper = new ZooKeeper("10.155.20.155:2181", 5000, new Exist_API_Sync_Usage());
        connectedSemaphore.await();

        zookeeper.exists(path, true);

        zookeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zookeeper.setData(path, "123".getBytes(), -1);

        zookeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zookeeper.delete(path + "/c1", -1);

        zookeeper.delete(path, -1);


        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event " + watchedEvent);
        try {
            if (Event.KeeperState.SyncConnected == watchedEvent.getState() && null == watchedEvent.getPath()) {
                connectedSemaphore.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {

                System.out.println("Node("+watchedEvent.getPath()+")DataChanged");
                zookeeper.exists(watchedEvent.getPath(),true);
            }else if(Event.EventType.NodeCreated==watchedEvent.getType()){
                System.out.println("Node("+watchedEvent.getPath()+")Created");
                zookeeper.exists(watchedEvent.getPath(),true);
            }else if(Event.EventType.NodeDeleted==watchedEvent.getType()){
                System.out.println("Node("+watchedEvent.getPath()+")Deleted");
                zookeeper.exists(watchedEvent.getPath(),true);
            }
        } catch (Exception e) {

        }
    }
}
