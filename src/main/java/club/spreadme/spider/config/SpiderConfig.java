package club.spreadme.spider.config;

import club.spreadme.spider.model.ContentType;
import club.spreadme.spider.model.CookieSpecs;
import club.spreadme.spider.model.HttpMethod;
import club.spreadme.spider.model.Proxy;

import java.io.Serializable;
import java.util.*;

public class SpiderConfig implements Serializable{

    private static final long serialVersionUID = -6957066045852669705L;
    private static final String DEFAULTAGENT="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36";
    
    private String domain;
    private String urlRegex;
    private String wantedUrlRegex;

    private List<String> seedLst = new ArrayList<>();
    private HttpMethod httpMethod = HttpMethod.GET;
    private ContentType contentType=ContentType.HTML;
    private int requestTimeout = 5 * 1000;
    private int socketTimeout = 5 * 1000;
    private int connectTimeout = 5 * 1000;
    private int sleepTime=0;
    private String charset = "utf-8";
    private CookieSpecs cookieSpec = CookieSpecs.STANDARD;
    private boolean isUseGzip = true;
    private int spiderCount = Runtime.getRuntime().availableProcessors()+1;
    private String userAgent= DEFAULTAGENT;
    private int retryTimes;
    private Proxy proxy;
    private boolean circularRedirectsAllowed=true;
    private int maxPerRoute=100;
    // default end date custom
    private String endDate;

    private Map<String, String> cookies = new LinkedHashMap<>();
    private Map<String, String> params = new LinkedHashMap<>();
    private Map<String, String> headers = new LinkedHashMap<>();
    private Map<String, String> formDatas = new LinkedHashMap<>();

    public String getDomain() {
        return domain;
    }

    public SpiderConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public List<String> getSeeds() {
        return seedLst;
    }

    public SpiderConfig setSeeds(String... seeds) {
        Arrays.stream(seeds).forEach(item -> seedLst.add(item));
        return this;
    }

    public String getUrlRegex() {
        return urlRegex;
    }

    public SpiderConfig setUrlRegex(String urlRegex) {
        this.urlRegex = urlRegex;
        return this;
    }

    public String getWantedUrlRegex() {
        return wantedUrlRegex;
    }

    public SpiderConfig setWantedUrlRegex(String wantedUrlRegex) {
        this.wantedUrlRegex = wantedUrlRegex;
        return this;
    }

    public int getSpiderCount() {
        return spiderCount;
    }

    public SpiderConfig setSpiderCount(int spiderCount) {
        this.spiderCount = spiderCount;
        return this;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public SpiderConfig setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public SpiderConfig setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public SpiderConfig setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public SpiderConfig setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public SpiderConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public SpiderConfig setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public CookieSpecs getCookieSpec() {
        return cookieSpec;
    }

    public SpiderConfig setCookieSpec(CookieSpecs cookieSpec) {
        this.cookieSpec = cookieSpec;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public SpiderConfig setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public SpiderConfig setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public boolean isUseGzip() {
        return isUseGzip;
    }

    public SpiderConfig setUseGzip(boolean useGzip) {
        isUseGzip = useGzip;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public SpiderConfig setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public boolean isCircularRedirectsAllowed() {
        return circularRedirectsAllowed;
    }

    public void setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
        this.circularRedirectsAllowed = circularRedirectsAllowed;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public SpiderConfig setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public SpiderConfig setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public SpiderConfig setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getFormDatas() {
        return formDatas;
    }

    public SpiderConfig setFormDatas(Map<String, String> formDatas) {
        this.formDatas = formDatas;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public SpiderConfig setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public SpiderConfig setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public SpiderConfig setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }
 
}
