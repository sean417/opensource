package com.learning.opensource.zk.curator.stages.choiceMaster;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by  on 17/11/23.
 *
 * 利用原生zk选主
 */
public class ChooseMaster implements Watcher {


    private ZooKeeper zk=null;
    private String selfPath=null;
    private String waitPath=null;
    private static final String ZK_ROOT_PATH="/zkmaster";  //选主从的跟路径
    private static final String ZK_SUB_PATH=ZK_ROOT_PATH+"/register";
    private CountDownLatch successCountDownLatch=new CountDownLatch(1);
    private CountDownLatch threadCompleteLatch=null;

    public ChooseMaster(CountDownLatch countDownLatch){

        this.threadCompleteLatch=countDownLatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {  //监听事件

        Event.KeeperState keeperState=watchedEvent.getState(); 
        Event.EventType eventType=watchedEvent.getType();
        if(Event.KeeperState.SyncConnected==keeperState){  //建立连接

            if(Event.EventType.None==eventType){

                System.out.println(Thread.currentThread().getName()+" connected to server");
                successCountDownLatch.countDown();
            }else if(Event.EventType.NodeDeleted==eventType && watchedEvent.getPath().equals(waitPath)){ //监测到节点删除，且为当前线程的等待节点

                System.out.println(Thread.currentThread().getName() + " some node was deleted,I'll check if I am the minimum node");
                try{

                    if(checkMinPath()){  //判断自己是不是最小的编号

                        processMasterEvent();  //处理主节点做的事情
                    }
                }catch (Exception e){

                    e.printStackTrace();
                }

            }

        }else if(Event.KeeperState.Disconnected==keeperState){  //连接断开

            System.out.println(Thread.currentThread().getName()+ " release connection");
        }else if(Event.KeeperState.Expired==keeperState){  //超时

            System.out.println(Thread.currentThread().getName()+ " connection expire");
        }
    }


    public void chooseMaster() throws Exception {

        selfPath=zk.create(ZK_SUB_PATH,null,ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);  //创建临时节点
        System.out.println(Thread.currentThread().getName()+ "create path "+selfPath);
        if(checkMinPath()){  //判断是否为主节点

            processMasterEvent();  
        }
    }

    public boolean createPersistPath(String path,String data,boolean needWatch) throws KeeperException, InterruptedException {

        if(zk.exists(path,needWatch)==null){

            zk.create(path,data.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            System.out.println(Thread.currentThread().getName()+" create persist path "+path);
        }
        return true;


    }

    public void createConnection(String connection,int timeout) throws IOException, InterruptedException {

        zk=new ZooKeeper(connection,timeout,this);
        successCountDownLatch.await();

    }

    private void processMasterEvent() throws KeeperException, InterruptedException {

        if(zk.exists(selfPath,false)==null){

            System.out.println(Thread.currentThread().getName()+ " selfnode is not exist "+ selfPath);
            return;
        }
        System.out.println(Thread.currentThread().getName()+ " I'm the master,now do work");
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName()+" Finish do work,leave master");
        //zk.delete(selfPath,-1);
        releaseConnection();
        threadCompleteLatch.countDown();

    }

    private void releaseConnection() {

        if(zk!=null){

            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkMinPath() throws Exception {

        //获取根节点下的所有子节点，进行排序，取当前路径的index，如果排在第一个，则为主，否则检测前一个节点是否存在，不存在则重新选举最小的节点
        List<String> subNodes=zk.getChildren(ZK_ROOT_PATH,false);
        System.out.println(subNodes.toString());
        Collections.sort(subNodes);
        System.out.println(Thread.currentThread().getName()+" tmp node index is "+selfPath.substring(ZK_ROOT_PATH.length()+1));
        int index=subNodes.indexOf(selfPath.substring(ZK_ROOT_PATH.length()+1));
        switch (index){

            case -1:
                System.out.println(Thread.currentThread().getName()+" create node is not exist");
                return false;
            case 0:
                System.out.println(Thread.currentThread().getName()+" I'm the master");
                return true;
            default:
                waitPath=ZK_ROOT_PATH+"/"+subNodes.get(index-1);
                System.out.println(Thread.currentThread().getName()+" the node before me is "+waitPath);
                try{

                    zk.getData(waitPath,true,new Stat());
                    return false;
                }catch (Exception e){

                    if(zk.exists(waitPath,false)==null){

                        System.out.println(Thread.currentThread().getName()+" the node before me is not exist,now is me");
                        return checkMinPath();
                    }else{

                        throw e;
                    }
                }
        }

    }
}