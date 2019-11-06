package com.learning.opensource.zk.curator.tools;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 16:54
 * 递归创建，递归删除节点等
 */
public class ZKPaths_Sample {
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
    static String path="/curator_zkpath_path";
    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();

    public static void main(String[] args) throws Exception{
        client.start();
        ZooKeeper zooKeeper=client.getZookeeperClient().getZooKeeper();
        System.out.println(ZKPaths.fixForNamespace(path,"sub"));
        System.out.println(ZKPaths.makePath(path,"sub"));

        System.out.println(ZKPaths.getNodeFromPath("/curator_zkpath_sample/sub1"));

        ZKPaths.PathAndNode pn=ZKPaths.getPathAndNode("/curator_zkpath_sample/sub1");
        System.out.println(pn.getPath());
        System.out.println(pn.getNode());

        String dir1=path+"/child1";
        String dir2=path+"/child2";

        ZKPaths.mkdirs(zooKeeper,dir1);
        ZKPaths.mkdirs(zooKeeper,dir2);

        System.out.println(ZKPaths.getSortedChildren(zooKeeper,path));

        ZKPaths.deleteChildren(client.getZookeeperClient().getZooKeeper(),path,true);

        System.out.println(ZKPaths.getPathAndNode("/").getPath());

        int aa=0;
        int bb=aa+1;

    }
}
