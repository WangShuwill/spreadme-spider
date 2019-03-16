package club.spreadme.spider.downloader.impl;

import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.downloader.ResultParser;
import club.spreadme.spider.model.Result;

public class JsonResultParser implements ResultParser
{

    private SpiderConfig config;

    public JsonResultParser(SpiderConfig config) {
        this.config = config;
    }

    @Override
    public Result parseResult(Result result) {
        result.setContentType(config.getContentType());
        return result;
    }

}
