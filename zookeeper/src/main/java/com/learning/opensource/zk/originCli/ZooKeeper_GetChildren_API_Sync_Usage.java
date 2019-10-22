package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 *
 * 打印结果：
Received watched event WatchedEvent state:SyncConnected type:None path:null
Success create znode:/zk11
[c]
Received watched event WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/zk11
ReGetChild:[c, c2]


 */
public class ZooKeeper_GetChildren_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);
    private static ZooKeeper zookeeper= null;

    public static void main(String[] args) throws  Exception{
        String path="/zk11";
        zookeeper=new ZooKeeper("10.155.20.155:2181",5000,new ZooKeeper_GetChildren_API_Sync_Usage());

        connectedSemaphore.await();
        //OPEN_ACL_UNSAFE 表示权限不受控制
        String path1=zookeeper.create(path,"sss".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Success create znode:"+path1);

        String path2=zookeeper.create(path+"/3","sss111".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        //调用getChildren的同时，注册了一个Watcher。
        List<String> childrenList=zookeeper.getChildren(path,true);
        System.out.println(childrenList);
        zookeeper.create(path+"/c4","sss222".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);


    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event "+watchedEvent);
        if(Event.KeeperState.SyncConnected== watchedEvent.getState()&&null==watchedEvent.getPath()){
            connectedSemaphore.countDown();
        }else if(watchedEvent.getType()== Event.EventType.NodeChildrenChanged){
            try {
                System.out.println("ReGetChild:" + zookeeper.getChildren(watchedEvent.getPath(), true));
            }catch (Exception e){}
        }
    }
}
