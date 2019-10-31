package com.learning.opensource.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/29 10:04
 *
 * 输出：
 *
 * Main thread: main
 * event[code:-4, type:WATCHED]
 * Thread of processResult:CuratorFramework-0
 * event[code:-4, type:WATCHED]
 * Thread of processResult:pool-3-thread-1
 */
public class Create_Node_Background_Sample {
    static String path="/zk-book-asy";
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);

    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();
    static CountDownLatch semaphore=new CountDownLatch(2);
    static ExecutorService tp= Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception{
        client.start();
        System.out.println("Main thread: "+Thread.currentThread().getName());

        //这里传入自定义的Executor
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[code:"+curatorEvent.getResultCode()+", type:"+curatorEvent.getType()+"]");
                        System.out.println("Thread of processResult:"+Thread.currentThread().getName());
                        semaphore.countDown();
                    }
                }
        ,tp).forPath(path,"init".getBytes());

        //这里没有传入自定义的Executor
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[code:"+curatorEvent.getResultCode()+", type:"+curatorEvent.getType()+"]");
                        System.out.println("Thread of processResult:"+Thread.currentThread().getName());
                        semaphore.countDown();
                    }
                }
                ).forPath(path,"init".getBytes());

        semaphore.await();
        tp.shutdown();
    }
}
