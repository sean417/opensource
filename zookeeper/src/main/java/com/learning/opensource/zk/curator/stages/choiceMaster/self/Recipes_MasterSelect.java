package com.learning.opensource.zk.curator.stages.choiceMaster.self;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/4 20:51
 * 利用curator-recipes选主
 */
public class Recipes_MasterSelect {
    static String master_path="/curator_recipes_master_path";
    static RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);

    static CuratorFramework client= CuratorFrameworkFactory.builder()
            .connectString("10.155.20.155:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy)
            .build();

    public static void main(String[] args) throws Exception{
        client.start();
        LeaderSelector selector=new LeaderSelector(client, master_path, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("成为 Master角色");
                Thread.sleep(1000);
                System.out.println("完成Master操作，释放Master权利");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);

    }
}
