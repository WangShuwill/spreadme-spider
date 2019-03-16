package club.spreadme.spider.scheduler.impl;

import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.scheduler.TriggerService;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wangshuwei
 * @since 2018-4-20
 */
public class DefaultTriggerService implements TriggerService {

    private SpiderConfig spiderConfig;

    @Override
    public boolean trigger() {
        return false;
    } 

    @Override
    public boolean begin() {
        return true;
    }

    @Override
    public boolean stop() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if(StringUtils.isBlank(spiderConfig.getEndDate())){
                return false;
            }
            Date endDate=dateFormat.parse(spiderConfig.getEndDate());
            return endDate.before(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public DefaultTriggerService setSpiderConfig(SpiderConfig spiderConfig) {
        this.spiderConfig = spiderConfig;
        return this;
    }
}
