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
 * 	第一次是正确的acl，成功。
 * 	第二次是错误的acl，失败。
 * 	init
 * Exception in thread "main" org.apache.zookeeper.KeeperException$NoAuthException: KeeperErrorCode = NoAuth for /zk-book-auth_test1
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:113)
 * 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:51)
 * 	at org.apache.zookeeper.ZooKeeper.getData(ZooKeeper.java:1212)
 * 	at org.apache.zookeeper.ZooKeeper.getData(ZooKeeper.java:1241)
 * 	at com.learning.opensource.zk.originCli.AuthSample_Get2.main(AuthSample_Get2.java:36)
 */
public class AuthSample_Get2 {

    final static String PATH="/zk-book-auth_test1";
    private static ZooKeeper zookeeper = null;
    public static void main(String[] args) throws Exception{
        zookeeper = new ZooKeeper("10.155.20.155:2181", 5000, null);
        zookeeper.addAuthInfo("digest","foo:true".getBytes());
        zookeeper.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);

        ZooKeeper zookeeper2 = new ZooKeeper("10.155.20.155:2181", 5000, null);
        zookeeper2.addAuthInfo("digest","foo:true".getBytes());
        System.out.println(new String(zookeeper2.getData(PATH,false,null)));

        ZooKeeper zookeeper3 = new ZooKeeper("10.155.20.155:2181", 5000, null);
        zookeeper3.addAuthInfo("digest","foo:false".getBytes());
        System.out.println(new String(zookeeper3.getData(PATH,false,null)));

        Thread.sleep(Integer.MAX_VALUE);
    }
}
