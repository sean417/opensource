package com.learning.opensource.zk.curator.stages.barrier;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 16:22
 */
public class Recipes_Barrier {
    static String barrier_path="/curator_recipes_barrier_path";
    static DistributedBarrier barrier;
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);

    public static void main(String[] args) throws Exception{
        for(int i=0;i<5;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CuratorFramework client= CuratorFrameworkFactory.builder()
                                .connectString("10.155.20.155:2181")
                                .sessionTimeoutMs(5000)
                                .retryPolicy(retryPolicy)
                                .build();
                        client.start();
                        barrier=new DistributedBarrier(client,barrier_path);
                        System.out.println(Thread.currentThread().getName()+"号barrier设置");
                        barrier.setBarrier();
                        barrier.waitOnBarrier();
                        System.err.println("启动......");
                    }catch (Exception e){

                    }
                }
            }).start();
        }

        Thread.sleep(2000);
        barrier.removeBarrier();
    }

}
