package club.spreadme.spider.downloader.impl;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.downloader.ResultParserFactory;
import club.spreadme.spider.model.Result;

/**
 * Reponse Handler,get content from reponse and parse,get regex urls
 *
 * @author Wangshuwei
 * @since 2018-4-24
 */
public class CommonReponseHandler implements ResponseHandler<Result> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonReponseHandler.class);

    private SpiderConfig spiderConfig;
    private String targetUrl;

    public CommonReponseHandler(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public Result handleResponse(HttpResponse httpResponse) throws IOException {
        int statuscode = httpResponse.getStatusLine().getStatusCode();
        if (statuscode == 200) {
            HttpEntity entity = httpResponse.getEntity();
            String content = IOUtils.toString(entity.getContent(), spiderConfig.getCharset());
            Result result = new Result();
            result.setTargetUrl(targetUrl);
            result.setStatuscode(statuscode);
            result.setContent(content);
            result.setHttpMethod(spiderConfig.getHttpMethod());
            ResultParserFactory.getResultParser(spiderConfig).parseResult(result);
            return result;

        } else {
            LOGGER.error("get html {} is error,statuscode is {} ", targetUrl, httpResponse.getStatusLine().getStatusCode());
            return null;
        }
    }

    public CommonReponseHandler setSpiderConfig(SpiderConfig spiderConfig) {
        this.spiderConfig = spiderConfig;
        return this;
    }
}
