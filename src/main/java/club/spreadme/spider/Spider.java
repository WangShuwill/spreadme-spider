package club.spreadme.spider;

import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.deduplication.DeduplicationService;
import club.spreadme.spider.deduplication.impl.BloomDeduplicationService;
import club.spreadme.spider.downloader.DownloadService;
import club.spreadme.spider.downloader.impl.DefaultDownloadService;
import club.spreadme.spider.model.Result;
import club.spreadme.spider.processor.ProcessService;
import club.spreadme.spider.processor.impl.LoggerProcessService;
import club.spreadme.spider.scheduler.SchedulerService;
import club.spreadme.spider.scheduler.ThreadService;
import club.spreadme.spider.scheduler.TriggerService;
import club.spreadme.spider.scheduler.impl.DefaultSchedulerService;
import club.spreadme.spider.scheduler.impl.DefaultThreadService;
import club.spreadme.spider.scheduler.impl.DefaultTriggerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class Spider implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Spider.class);

    private boolean isStop;
    private ReentrantLock lock = new ReentrantLock();
    private Condition conditionLock = lock.newCondition();

    private SpiderConfig spiderConfig;
    private ThreadService threadService;
    private ProcessService processService=new LoggerProcessService();
    private DownloadService downloadService;
    private DeduplicationService deduplicationService=new BloomDeduplicationService();
    private SchedulerService schedulerService=new DefaultSchedulerService();
    private TriggerService triggerService;

    private void initServices() throws Exception {
        if (spiderConfig == null) {
            throw new Exception("No SpiderConfig 😥");
        }
        if (StringUtils.isBlank(spiderConfig.getDomain())) {
            throw new Exception("No SpiderConfig Domain 😂");
        }
        if (spiderConfig.getSeeds().size() == 0) {
            throw new Exception("No SpiderConfig Seeds 😂");
        }
        if (triggerService == null || downloadService==null) {
            triggerService = new DefaultTriggerService().setSpiderConfig(spiderConfig);
            downloadService=new DefaultDownloadService().setSpiderConfig(spiderConfig);
        }

        schedulerService = schedulerService.setDeduplicationService(deduplicationService);
        threadService = new DefaultThreadService(spiderConfig.getSpiderCount());

        addUrl(spiderConfig.getSeeds());
    }

    public void run() {
        // Component init
        LOGGER.info("The Spider is runing 🙂");
        LOGGER.info("The Spider init basic service ⚙");
        
        try {
            initServices();  
        } catch (Exception e) {
            LOGGER.error("The Spider init fail {}", e.toString());
            return;
        }

        while (!Thread.currentThread().isInterrupted() && !isStop) {
            final String url = schedulerService.poll();
            if(StringUtils.isNotBlank(url)){
                threadService.execute(() -> {
                    process(downloadService.getResult(url));
                    trigger();
                });
            }else if(threadService.getAliveCount() == 0){
                break;
            }

            sleep(spiderConfig.getSleepTime());
        }
       
        close();
    }

    private void process(Result result){
        if (result != null) {
            if (isWantedUrl(result.getTargetUrl())) {
                processService.process(result);
            }
            addUrl(result.getLinks());
        }
    }

    private boolean isWantedUrl(String url) {
        if (StringUtils.isBlank(spiderConfig.getWantedUrlRegex())) {
            return true;
        } else {
            Pattern pattern = Pattern.compile(spiderConfig.getWantedUrlRegex());
            return pattern.matcher(url).find();
        }
    }

    private void addUrl(List<String> urls) {
        for (String url : urls) {
            schedulerService.push(url);
        }
    }

    private void trigger(){
        try {
            lock.lock();
            isStop = triggerService.stop();
            conditionLock.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void sleep(int time){
        if (time != 0) {
            try {
                Thread.sleep(spiderConfig.getSleepTime());
            } catch (InterruptedException e) {
                LOGGER.error("The Thread sleep is error {}", e.getMessage());
            }
        }
    }

    private void close() {
        threadService.close();
    }

    public Spider setSpiderConfig(SpiderConfig spiderConfig) {
        this.spiderConfig = spiderConfig;
        return this;
    }

    public Spider setProcessService(ProcessService processService) {
        this.processService = processService;
        return this;
    }

    public Spider setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
        return this;
    }

    public Spider setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
        return this;
    }

    public Spider setDeduplicationService(DeduplicationService deduplicationService) {
        this.deduplicationService = deduplicationService;
        return this;
    }

    public Spider setTriggerService(TriggerService triggerService) {
        this.triggerService = triggerService;
        return this;
    }
}
