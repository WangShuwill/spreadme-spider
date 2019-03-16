package club.spreadme.spider.downloader.impl;

import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.downloader.DownloadService;
import club.spreadme.spider.model.Result;

public class DefaultDownloadService implements DownloadService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultDownloadService.class);

    private SpiderConfig spiderConfig;

    @Override
    public Result getResult(String targetUrl) {
        try (CloseableHttpClient httpClient = HttpClientPoolManager.instanceClient(spiderConfig)) {

            ResponseHandler<Result> responseHandler=new CommonReponseHandler(targetUrl).setSpiderConfig(spiderConfig);
            return httpClient.execute(getHttpUriRequest(targetUrl),responseHandler,getHttpClientContext());

        } catch (Exception e) {
            LOGGER.error("get html {} is error {} ", targetUrl, e.toString());
            return null;
        }
    }

    private HttpClientContext getHttpClientContext() {
        HttpClientContext clientContext = new HttpClientContext();
        if (spiderConfig.getCookies() != null && !spiderConfig.getCookies().isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            for (Map.Entry<String, String> cookieEntry : spiderConfig.getCookies().entrySet()) {
                BasicClientCookie clientCookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                clientCookie.setDomain(spiderConfig.getDomain());
                cookieStore.addCookie(clientCookie);
            }
            clientContext.setCookieStore(cookieStore);
        }
        return clientContext;
    }

    private HttpUriRequest getHttpUriRequest(String url) {
        RequestBuilder requestBuilder = RequestBuilder.create(spiderConfig.getHttpMethod().getValue()).setUri(url);
        // headers
        for (Map.Entry<String, String> headerEntry : spiderConfig.getHeaders().entrySet()) {
            requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        // params
        for(Map.Entry<String,String> param:spiderConfig.getParams().entrySet()){
            NameValuePair nameValuePair=new BasicNameValuePair(param.getKey(),param.getValue());
            requestBuilder.addParameter(nameValuePair);
        }

        RequestConfig.Builder requestConfig = RequestConfig.custom();
        requestConfig.setConnectionRequestTimeout(spiderConfig.getRequestTimeout())
                .setConnectTimeout(spiderConfig.getConnectTimeout())
                .setSocketTimeout(spiderConfig.getSocketTimeout())
                .setCookieSpec(spiderConfig.getCookieSpec().getValue())
                .setCircularRedirectsAllowed(spiderConfig.isCircularRedirectsAllowed());
        
        requestBuilder.setConfig(requestConfig.build());
        HttpUriRequest uriRequest = requestBuilder.build();
        if (spiderConfig.getHeaders() != null && !spiderConfig.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> header : spiderConfig.getHeaders().entrySet()) {
                uriRequest.addHeader(header.getKey(), header.getValue());
            }
        }

        return uriRequest;
    }

    public DownloadService setSpiderConfig(SpiderConfig spiderConfig) {
        this.spiderConfig = spiderConfig;
        return this;
    }

}
