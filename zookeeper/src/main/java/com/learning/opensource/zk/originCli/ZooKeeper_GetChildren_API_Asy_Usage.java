package com.learning.opensource.zk.originCli;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/21 18:47
 *
 * 打印结果：
 * 异步获取子节点列表和节点的数据
Success create znode:/zk22
Get Children znode result:[response code: 0, param path:/zk22,ctx:null, children list:[c1], stat: 4294967322,4294967322,1571800838889,1571800838889,0,1,0,0,3,1,4294967323

Received watched event WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/zk22
ReGetChild:[c1, c2]


 */
public class ZooKeeper_GetChildren_API_Asy_Usage implements Watcher{
    private static CountDownLatch connectedSemaphore=new CountDownLatch(1);
    private static ZooKeeper zookeeper= null;

    public static void main(String[] args) throws  Exception{
        String path="/zk22";
        zookeeper=new ZooKeeper("10.155.20.155:2181",5000,new ZooKeeper_GetChildren_API_Asy_Usage());

        connectedSemaphore.await();
        //OPEN_ACL_UNSAFE 表示权限不受控制
        String path1=zookeeper.create(path,"sss".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Success create znode:"+path1);

        String path2=zookeeper.create(path+"/c1","sss111".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        //调用getChildren的同时，注册了一个Watcher。
        zookeeper.getChildren(path,true,new IChildren2Callback(),null);
        zookeeper.create(path+"/c2","sss222".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

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
class IChildren2Callback implements AsyncCallback.Children2Callback{
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        System.out.println("Get Children znode result:[response code: "+i+", param path:"+s+",ctx:"+o+", children list:"+list+", stat: "+stat);
    }
}