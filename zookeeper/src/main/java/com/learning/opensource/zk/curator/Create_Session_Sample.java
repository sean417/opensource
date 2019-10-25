package com.learning.opensource.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/25 17:28
 */
public class Create_Session_Sample {
    public static void main(String[] args) throws Exception{
        RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
        CuratorFramework client= CuratorFrameworkFactory.newClient("10.155.20.155:2181",5000,3000,retryPolicy);
        System.out.println(client.getState());
        client.start();
        System.out.println(client.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }
}
