package club.spreadme.spider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.deduplication.DeduplicationService;
import club.spreadme.spider.downloader.DownloadService;
import club.spreadme.spider.processor.ProcessService;
import club.spreadme.spider.scheduler.SchedulerService;
import club.spreadme.spider.scheduler.TriggerService;

public class SpiderNest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderNest.class);

    public Spider build(SpiderConfig spiderConfig, Object... services) {
        Spider spider = new Spider();
        spider.setSpiderConfig(spiderConfig);
        loadServices(spider, services);
        return spider;
    }

    public Spider build(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            return build(properties);
        } catch (IOException e) {
            LOGGER.error("SpiderNest build error {}", e.getMessage());
            return new Spider();
        }
    }

    public Spider build(String propertiesPath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(propertiesPath)));
            return build(properties);
        } catch (Exception e) {
            LOGGER.error("SpiderNest build error {}", e.getMessage());
            return new Spider();
        }
    }

    public Spider build(Properties properties) {
        SpiderConfig spiderConfig = new SpiderConfig();
        Spider spider = new Spider();
        spider.setSpiderConfig(spiderConfig);

        List<Object> services = new ArrayList<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            // spideconfig
            String key = String.valueOf(enumeration.nextElement());
            if (StringUtils.isNotBlank(key) && key.startsWith("spideconfig")) {
                String fieldName = key.replaceAll("spideconfig.", "").trim();
                String fieldValue = properties.getProperty(key);
                if ("seeds".equals(fieldName)) {
                    spiderConfig.setSeeds(fieldValue.split(","));
                } else {
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        // TODO 先兼容String类型
                        Method method = spiderConfig.getClass().getMethod(setMethodName, String.class);
                        method.invoke(spiderConfig, fieldValue);
                    } catch (Exception e) {
                        LOGGER.error("SpiderNest build error {}", e.getMessage());
                    }
                }
            }

            // spider services
            if (StringUtils.isNotBlank(key) && key.contains("spiderservice")) {
                String serviceValue = properties.getProperty(key);
                try {
                    Object service = Class.forName(serviceValue).newInstance();
                    services.add(service);
                } catch (Exception e) {
                    LOGGER.error("SpiderNest build error {}", e.getMessage());
                }
            }
        }

        loadServices(spider, services);
        return spider;
    }

    protected void loadServices(Spider spider, Object... services) {
        for (Object service : services) {
            if (service instanceof SchedulerService) {
                spider.setSchedulerService((SchedulerService) service);
            } else if (service instanceof DeduplicationService) {
                spider.setDeduplicationService((DeduplicationService) service);
            } else if (service instanceof TriggerService) {
                spider.setTriggerService((TriggerService) service);
            } else if (service instanceof ProcessService) {
                spider.setProcessService((ProcessService) service);
            } else if (service instanceof DownloadService) {
                spider.setDownloadService((DownloadService) service);
            }
        }
    }

    protected void loadServices(Spider spider, List<Object> services) {
        Object[] serviceArr = new Object[services.size()];
        loadServices(spider, services.toArray(serviceArr));
    }
}