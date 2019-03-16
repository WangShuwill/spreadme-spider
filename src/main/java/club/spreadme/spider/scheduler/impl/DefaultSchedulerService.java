package club.spreadme.spider.scheduler.impl;

import club.spreadme.spider.deduplication.DeduplicationService;
import club.spreadme.spider.deduplication.impl.BloomDeduplicationService;
import club.spreadme.spider.scheduler.SchedulerService;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class DefaultSchedulerService implements SchedulerService {

    private DeduplicationService deduplicationService = new BloomDeduplicationService();
    private BlockingDeque<String> deque = new LinkedBlockingDeque<>();

    @Override
    public SchedulerService setDeduplicationService(DeduplicationService deduplicationService) {
        this.deduplicationService = deduplicationService;
        return this;
    }

    @Override
    public void push(String url) {
        if (deduplicationService.isNoRepeat(url)) {
            deque.add(url);
        }
    }

    @Override
    public String poll() {
        return deque.poll();
    }

}
