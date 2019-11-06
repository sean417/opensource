package com.learning.opensource.zk.curator.tools;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 17:24
 * 确保节点存在，不存在就创建，存在就不创建，同时解决并发问题。
 */
public class EnsurePathDemo {
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
    static String path="/zk-book/c1";
    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();

    public static void main(String[] args) throws Exception{
        client.start();
        client.usingNamespace("zk-book");
        EnsurePath ensurePath=new EnsurePath(path);
        ensurePath.ensure(client.getZookeeperClient());
        ensurePath.ensure(client.getZookeeperClient());

        EnsurePath ensurePath1=client.newNamespaceAwareEnsurePath("/c1");
        ensurePath1.ensure(client.getZookeeperClient());
    }

}
