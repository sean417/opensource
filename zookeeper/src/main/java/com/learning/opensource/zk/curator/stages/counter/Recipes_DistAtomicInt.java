package com.learning.opensource.zk.curator.stages.counter;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 15:55
 * 分布式计数器
 */
public class Recipes_DistAtomicInt {
    static String distatomicint_path="/curator_recipes_distatomicint_path";
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();

    public static void main(String[] args) throws Exception{
            client.start();
        DistributedAtomicInteger atomicInteger=
                new DistributedAtomicInteger(client,distatomicint_path,new RetryNTimes(3,1000));
        AtomicValue<Integer> rc=atomicInteger.add(8);
        System.out.println("Result: "+rc.succeeded()+" preValue: "+rc.preValue()+"postValue: "+rc.postValue());
    }
}
