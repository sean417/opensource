package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 *
 * 打印结果：节点数据内容或节点版本变化了，都视为zookeeper节点的变化。
Received watched event WatchedEvent state:SyncConnected type:None path:null
123
4294967326,4294967326,0
Received watched event WatchedEvent state:SyncConnected type:NodeDataChanged path:/zk-book
123
4294967326,4294967327,1

 */
public class GetData_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);
    private static ZooKeeper zookeeper= null;
    private static Stat stat=new Stat();

    public static void main(String[] args) throws  Exception{
        String path="/zk-book";
        zookeeper=new ZooKeeper("10.155.20.155:2181",5000,new GetData_API_Sync_Usage());

        connectedSemaphore.await();
        //OPEN_ACL_UNSAFE 表示权限不受控制
        String path1=zookeeper.create(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zookeeper.getData(path,true,stat)));

        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());

        zookeeper.setData(path,"123".getBytes(),-1);
        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event "+watchedEvent);
        if(Event.KeeperState.SyncConnected== watchedEvent.getState()&&null==watchedEvent.getPath()){
            connectedSemaphore.countDown();
        }else if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
            try {
                System.out.println(new String(zookeeper.getData(watchedEvent.getPath(),true,stat)));
                System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
            }catch (Exception e){}
        }
    }
}
