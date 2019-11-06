package com.learning.opensource.zk.curator.stages.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 15:43
 */
public class Recipes_Lock {

    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
    static String lock_path="/curator_recipes_lock_path";
    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();

    public static void main(String[] args) {
        client.start();
        final InterProcessMutex lock=new InterProcessMutex(client,lock_path);
        final CountDownLatch down=new CountDownLatch(1);
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        down.await();
                        lock.acquire();//阻塞等待获得锁
                    }catch (Exception e){

                    }
                    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo=sdf.format(new Date());
                    System.out.println("生成的订单号是："+orderNo);
                    try {
                        lock.release();// 释放分布式锁资源
                    }catch (Exception e){}
                }
            }).start();
        }
        down.countDown();
    }

}
