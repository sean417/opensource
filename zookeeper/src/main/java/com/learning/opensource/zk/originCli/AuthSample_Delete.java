package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/23 17:07
 *删除节点失败：KeeperErrorCode = NoAuth for /zk-book-auth_test2/child
 * 成功删除节点：/zk-book-auth_test2/child
 * 成功删除节点：/zk-book-auth_test2
 *
 * 删除节点的acl，权限只会作用于子节点，而对于当前节点无效
 */
public class AuthSample_Delete {

    final static String PATH="/zk-book-auth_test2";
    final static String PATH2="/zk-book-auth_test2/child";

    private static ZooKeeper zookeeper = null;
    public static void main(String[] args) throws Exception{
        ZooKeeper zookeeper1 = new ZooKeeper("10.155.20.155:2181", 5000, null);
        zookeeper1.addAuthInfo("digest","foo:true".getBytes());
        zookeeper1.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zookeeper1.create(PATH2,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        try{
            ZooKeeper zookeeper2 = new ZooKeeper("10.155.20.155:2181", 5000, null);
            zookeeper2.delete(PATH2,-1);
        }catch (Exception e){
            System.out.println("删除节点失败："+e.getMessage());
        }
        ZooKeeper zookeeper3 = new ZooKeeper("10.155.20.155:2181", 5000, null);
        zookeeper3.addAuthInfo("digest","foo:true".getBytes());
        zookeeper3.delete(PATH2,-1);
        System.out.println("成功删除节点："+PATH2);

        ZooKeeper zookeeper4 = new ZooKeeper("10.155.20.155:2181", 5000, null);
        zookeeper4.delete(PATH,-1);
        System.out.println("成功删除节点："+PATH);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
