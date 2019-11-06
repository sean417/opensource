package com.learning.opensource.zk.curator.stages.barrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenyang@koolearn-inc.com
 * @date 2019/11/6 16:12
 */
public class Recipes_CyclicBarrier {

    public static CyclicBarrier barrier= new CyclicBarrier(3);

    public static void main(String[] args) {
        ExecutorService executor= Executors.newFixedThreadPool(3);
        executor.submit(new Thread(new Thread(new Runner("1号"))));
        executor.submit(new Thread(new Thread(new Runner("2号"))));
        executor.submit(new Thread(new Thread(new Runner("3号"))));
        executor.shutdown();

    }
}

class Runner implements Runnable{
    private String name;

    public Runner(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name+" 准备好了");
        try{
            Recipes_CyclicBarrier.barrier.await();
        }catch (Exception ex){}
        System.out.println(name+" 起跑！！！");
    }
}
