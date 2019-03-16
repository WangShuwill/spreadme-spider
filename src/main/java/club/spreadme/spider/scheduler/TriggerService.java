package club.spreadme.spider.scheduler;

/**
 * @author Wangshuwei
 * @since 2018-4-20
 */
public interface TriggerService {

     boolean trigger();
     
     boolean begin();
     
     boolean stop();
}
