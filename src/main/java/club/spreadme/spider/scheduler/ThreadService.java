package club.spreadme.spider.scheduler;

/**
 * @author Wangshuwei
 * @since 2018-4-23
 */
public interface ThreadService{

     int getAliveCount();

     void execute(Runnable runnable);

     void close();

}
