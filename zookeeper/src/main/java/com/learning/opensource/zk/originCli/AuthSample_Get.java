package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/23 17:07
 *
 * Received watched event WatchedEvent state:SyncConnected type:None path:null
 * Received watched event WatchedEvent state:SyncConnected type:None path:null
 * Exception in thread "main" org.apache.zookeeper.KeeperException$NoAuthException: KeeperErrorCode = NoAuth for /zk-book-auth_test
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:113)
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:51)
 * 	at org.apache.zookeeper.ZooKeeper.getData(ZooKeeper.java:1212)
 * 	at org.apache.zookeeper.ZooKeeper.getData(ZooKeeper.java:1241)
 *
 * 	验证没权限访问数据时会报错。
 */
public class AuthSample_Get {

    final static String PATH="/zk-book-auth_test";
    private static ZooKeeper zookeeper = null;
    public static void main(String[] args) throws Exception{
        zookeeper = new ZooKeeper("10.155.20.155:2181", 5000, new Exist_API_Sync_Usage());
        zookeeper.addAuthInfo("digest","foo:true".getBytes());
        zookeeper.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        ZooKeeper zookeeper2 = new ZooKeeper("10.155.20.155:2181", 5000, new Exist_API_Sync_Usage());
        zookeeper2.getData(PATH,false,null);
        Thread.sleep(Integer.MAX_VALUE);

    }
}
