package com.learning.opensource.zk.ZkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/24 19:57
 *
 * 打印结果：
123
Node /zk-book-zkClient9 changed,new data:456
Node /zk-book-zkClient9 deleted


 */
public class Get_Data_Sample {
    public static void main(String[] args) throws Exception{
        String path="/zk-book-zkClient9";
        ZkClient zkClient=new ZkClient("10.155.20.155:2181",5000);
        zkClient.createEphemeral(path,"123");

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("Node "+dataPath+" changed,new data:"+data);
            }

            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("Node "+dataPath+" deleted");
            }
        });

//        System.out.println(zkClient.readData(path));

        zkClient.writeData(path,"456");
        Thread.sleep(1000);

        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
