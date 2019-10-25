package com.learning.opensource.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/25 18:33
 */
public class Create_Node_Sample {
    static String path="/zk-book-curator/c1";
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);

    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();

    public static void main(String[] args) throws Exception{
        client.start();
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init".getBytes());
        Thread.sleep(Integer.MAX_VALUE);
    }
}
