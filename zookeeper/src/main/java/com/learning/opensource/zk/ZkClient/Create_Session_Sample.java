package com.learning.opensource.zk.ZkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/23 18:08
 */
public class Create_Session_Sample {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("10.155.20.155:2181",5000);
        System.out.println("zookeeper session established.");
    }
}
