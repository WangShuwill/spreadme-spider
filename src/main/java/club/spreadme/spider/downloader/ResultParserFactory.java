package club.spreadme.spider.downloader;

import club.spreadme.spider.downloader.impl.HtmlResultParser;
import club.spreadme.spider.downloader.impl.JsonResultParser;
import club.spreadme.spider.config.SpiderConfig;

public class ResultParserFactory {
    public static ResultParser getResultParser(SpiderConfig config) {
        ResultParser resultParser;
        switch (config.getContentType()) {
            case HTML:
                resultParser = new HtmlResultParser(config);
                break;
            case JSON:
                resultParser = new JsonResultParser(config);
                break;
            default:
                resultParser = new HtmlResultParser(config);
                break;
        }

        return resultParser;
    }
}
