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
Received watched event WatchedEvent state:SyncConnected type:NodeDataChanged path:/zk-book-setData5
4294967358,4294967359,1
4294967358,4294967360,2
KeeperErrorCode = BadVersion for /zk-book-setData5

 */
public class SetData_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);
    private static ZooKeeper zookeeper= null;
    private static Stat stat=new Stat();

    public static void main(String[] args) throws  Exception{
        String path="/zk-book-setData5";
        zookeeper=new ZooKeeper("10.155.20.155:2181",5000,new SetData_API_Sync_Usage());

        connectedSemaphore.await();
        //OPEN_ACL_UNSAFE 表示权限不受控制
        zookeeper.create(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zookeeper.getData(path,true,null);

        Stat stat=zookeeper.setData(path,"456".getBytes(),-1);
        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());

        Stat stat2=zookeeper.setData(path,"456".getBytes(),stat.getVersion());
        System.out.println(stat2.getCzxid()+","+stat2.getMzxid()+","+stat2.getVersion());
        try {
            zookeeper.setData(path,"456".getBytes(),stat.getVersion());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event "+watchedEvent);
        if(Event.KeeperState.SyncConnected== watchedEvent.getState()&&null==watchedEvent.getPath()){
            connectedSemaphore.countDown();
        }
    }
}
