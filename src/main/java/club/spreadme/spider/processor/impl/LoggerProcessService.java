package club.spreadme.spider.processor.impl;

import club.spreadme.spider.model.Result;
import club.spreadme.spider.processor.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Wangshuwei
 * @since 2018-4-21
 */
public class LoggerProcessService implements ProcessService{

    private static final Logger LOGGER= LoggerFactory.getLogger(LoggerProcessService.class);

    @Override
    public void process(Result content) {
        LOGGER.info("The Fetch URL is {}",content.getTargetUrl());
    }
}
