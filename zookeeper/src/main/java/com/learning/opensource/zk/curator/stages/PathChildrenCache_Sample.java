package com.learning.opensource.zk.curator.stages;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/4 19:51
 *
 * NodeCach用于监听指定ZooKeeper数据节点的子节点的变化。
 *
 * CHILD_ADDED,/zk-book/children84/c1
 * CHILD_UPDATED,/zk-book/children84/c1
 * CHILD_REMOVED,/zk-book/children84/c1
 * CHILD_REMOVED,/zk-book/children84/c1
 */
public class PathChildrenCache_Sample {
    static String path="/zk-book/children84";
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);

    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();

    public static void main(String[] args) throws Exception {
            client.start();

                PathChildrenCache cache=new PathChildrenCache(client,path,true);
                cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
                cache.getListenable().addListener(new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                        switch (event.getType()){
                            case CHILD_ADDED:
                                System.out.println("CHILD_ADDED,"+event.getData().getPath());
                            case CHILD_UPDATED:
                                System.out.println("CHILD_UPDATED,"+event.getData().getPath());
                            case CHILD_REMOVED:
                                System.out.println("CHILD_REMOVED,"+event.getData().getPath());
                                break;
                            default:
                                break;
                        }
                    }
                });
                client.create().withMode(CreateMode.PERSISTENT).forPath(path);
                Thread.sleep(1000);
                client.create().withMode(CreateMode.PERSISTENT).forPath(path+"/c1");
                Thread.sleep(1000);
                client.delete().forPath(path+"/c1");
                Thread.sleep(1000);
                client.delete().forPath(path);
                Thread.sleep(Integer.MAX_VALUE);
    }
}
