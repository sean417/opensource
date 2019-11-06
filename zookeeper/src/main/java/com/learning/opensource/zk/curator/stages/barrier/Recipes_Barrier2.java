package com.learning.opensource.zk.curator.stages.barrier;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 16:22
 */
public class Recipes_Barrier2 {
    static String barrier_path="/curator_recipes_barrier_path1";
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
                        DistributedDoubleBarrier barrier=new DistributedDoubleBarrier(client,barrier_path,5);
                        Thread.sleep(Math.round(Math.random())*3000);
                        System.out.println(Thread.currentThread().getName()+"号barrier设置");
                        barrier.enter();
                        System.out.println("启动......");
                        Thread.sleep(Math.round(Math.random())*3000);
                        barrier.leave();
                        System.err.println("退出......");
                    }catch (Exception e){

                    }
                }
            }).start();
        }
    }

}
