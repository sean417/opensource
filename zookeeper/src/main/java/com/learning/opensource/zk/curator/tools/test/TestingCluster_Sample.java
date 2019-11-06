package com.learning.opensource.zk.curator.tools.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingServer;
import org.apache.curator.test.TestingZooKeeperServer;

import java.io.File;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 17:37
 * 用于测试用的zk服务。
 * zk集群实现
 *
 */
public class TestingCluster_Sample {
    static String path="/zookeeper";



    public static void main(String[] args) throws Exception{
        TestingCluster cluster=new TestingCluster(3);
        cluster.start();
        Thread.sleep(2000);

        TestingZooKeeperServer leader=null;
        for(TestingZooKeeperServer zs:cluster.getServers()){
            System.out.println(zs.getInstanceSpec().getServerId()+"-");
            System.out.println(zs.getQuorumPeer().getServerState()+"-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
            if(zs.getQuorumPeer().getServerState().equals("leading")){
                leader=zs;
            }
        }

        leader.kill();
        System.out.println("--After leader kill:");
        Thread.sleep(2000);
        for(TestingZooKeeperServer zs:cluster.getServers()){
            System.out.println(zs.getInstanceSpec().getServerId()+"-");
            System.out.println(zs.getQuorumPeer().getServerState()+"-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
        }
        cluster.stop();
    }
}
