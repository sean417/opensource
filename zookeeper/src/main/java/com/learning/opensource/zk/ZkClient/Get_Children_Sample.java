package com.learning.opensource.zk.ZkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/24 19:57
 *
 * 打印结果：
/zk-book-zkClient's child changed,currentChilds:[]
[]
/zk-book-zkClient's child changed,currentChilds:[c1]
/zk-book-zkClient's child changed,currentChilds:[]
/zk-book-zkClient's child changed,currentChilds:null


 */
public class Get_Children_Sample {
    public static void main(String[] args) throws Exception{
        String path="/zk-book-zkClient";
        ZkClient zkClient=new ZkClient("10.155.20.155:2181",5000);

        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s+"'s child changed,currentChilds:"+list);
            }
        });

        zkClient.createPersistent(path);

        Thread.sleep(1000);
        System.out.println(zkClient.getChildren(path));
        Thread.sleep(1000);
        zkClient.createPersistent(path+"/c1");
        Thread.sleep(1000);
        zkClient.delete(path+"/c1");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
