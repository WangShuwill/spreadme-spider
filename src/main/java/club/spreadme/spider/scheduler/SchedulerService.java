package club.spreadme.spider.scheduler;

import club.spreadme.spider.deduplication.DeduplicationService;

public interface SchedulerService {
	
	 SchedulerService setDeduplicationService(DeduplicationService deduplicationService);
	
	 void push(String url);
	
	 String poll();

}
