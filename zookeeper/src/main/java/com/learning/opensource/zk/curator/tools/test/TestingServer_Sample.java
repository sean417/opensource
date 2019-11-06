package com.learning.opensource.zk.curator.tools.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import java.io.File;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 17:37
 * 用于测试用的zk服务。
 * zk单机实现
 *
 */
public class TestingServer_Sample {
    static String path="/zookeeper";



    public static void main(String[] args) throws Exception{
        TestingServer server=new TestingServer(2181,new File("/home/admin/zk-book-data"));//自定义端口和存储路径
         RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);

         CuratorFramework client= CuratorFrameworkFactory.builder()
                .connectString("10.155.20.155:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
         client.start();
         System.out.println(client.getChildren().forPath(path));
         server.close();
    }
}
