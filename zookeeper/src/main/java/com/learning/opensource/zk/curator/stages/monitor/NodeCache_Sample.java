package com.learning.opensource.zk.curator.stages.monitor;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/1 21:18
 * curator对事件监听的实现：
 * NodeCach用于监听指定ZooKeeper数据节点本身的变化
 */
public class NodeCache_Sample {
    static String path="/zk-book/nodecache";
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
        final NodeCache cache=new NodeCache(client,path,false);
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("node data update,new data:"+new String(cache.getCurrentData().getData()));
            }
        });
        client.setData().forPath(path,"u".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
