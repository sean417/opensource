package com.learning.opensource.zk.curator.stages.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 15:33
 *
 * 复现并发问题。
 * 生成的10个订单号很多都是重复的。
 */
public class Recipes_NoLock {
    public static void main(String[] args) {
        final CountDownLatch downLatch=new CountDownLatch(1);
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        downLatch.await();
                    }catch (Exception e){

                    }
                    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo=sdf.format(new Date());
                    System.out.println("生成的订单号是："+orderNo);
                }
            }).start();
        }
        downLatch.countDown();
    }
}
