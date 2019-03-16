package club.spreadme.spider.scheduler.impl;

import club.spreadme.spider.scheduler.ThreadService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Wangshuwei
 * @since 2018-4-23
 */
public class DefaultThreadService implements ThreadService{

    private int threadCount;
    private AtomicInteger aliveCount=new AtomicInteger();

    private ReentrantLock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();
    private static ExecutorService executorService;

    public DefaultThreadService(int threadCount){
        this.threadCount=threadCount;
        executorService= Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public int getAliveCount() {
        return aliveCount.get();
    }

    @Override
    public void execute(Runnable runnable) {
        if (aliveCount.get() >= threadCount) {
            try {
                lock.lock();
                while (aliveCount.get() >= threadCount) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        aliveCount.incrementAndGet();
        executorService.execute(()->{
            try{
                runnable.run();
            }finally {
                try{
                    lock.lock();
                    aliveCount.decrementAndGet();
                    condition.signal();
                }finally {
                    lock.unlock();
                }
            }
        });
    }

    @Override
    public void close() {
        executorService.shutdown();
    }
}
