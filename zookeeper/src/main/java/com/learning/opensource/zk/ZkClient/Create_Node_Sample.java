package com.learning.opensource.zk.ZkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/10/24 19:57
 *
 * 调用createPersistent接口创建持久节点，createParents为true，这样就会自动递归创建父节点。
 * 有对应的异步接口
 */
public class Create_Node_Sample {
    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient("10.155.20.155:2181",5000);
        String path="/zk-book/c1";
        zkClient.createPersistent(path,true);
    }
}
