package club.spreadme.spider.downloader.impl;

import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.model.HttpMethod;
import club.spreadme.spider.model.Proxy;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author Wangshuwei
 * @since 2018-4-16
 */
public class HttpClientPoolManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientPoolManager.class);

    private static PoolingHttpClientConnectionManager connectionManager;
    private SpiderConfig config;

    public static CloseableHttpClient instanceClient(SpiderConfig config) {
        return new HttpClientPoolManager(config).generateClient();
    }

    private HttpClientPoolManager(SpiderConfig config) {
        this.config = config;
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnetionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setDefaultMaxPerRoute(config.getMaxPerRoute());
        connectionManager.setMaxTotal(config.getSpiderCount());
    }

    private SSLConnectionSocketFactory buildSSLConnetionSocketFactory() {
        try {
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error("SSL Connection fail {}", e.getMessage());
        }

        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sc = SSLContext.getInstance("SSLv3");
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    private CloseableHttpClient generateClient() {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setUserAgent(config.getUserAgent());

        if (config.isUseGzip()) {
            clientBuilder.addInterceptorFirst((HttpRequestInterceptor) (httpRequest, httpContext) -> {
                if (!httpRequest.containsHeader("Accept-Encoding")) {
                    httpRequest.addHeader("Accept-Encoding", "gzip");
                }
            });
        }

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .build();
        clientBuilder.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultSocketConfig(socketConfig);
        clientBuilder.setConnectionManager(connectionManager);
        if (HttpMethod.POST.equals(config.getHttpMethod())) {
            clientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
        } else {
            clientBuilder.setRedirectStrategy(new DefaultRedirectStrategy());
        }
        clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(config.getRetryTimes(), true));
        generateCookie(clientBuilder);
        
        // Proxy
        if(config.getProxy()!=null){
            Proxy proxy=config.getProxy();
            LOGGER.info("SpiderConfig enable Proxy {}",proxy.toString());
            if(StringUtils.isNotBlank(proxy.getPassword())){
                CredentialsProvider credentialsProvider=new BasicCredentialsProvider();
                AuthScope authScope=new AuthScope(proxy.getHost(),proxy.getPort());
                Credentials credentials=new UsernamePasswordCredentials(proxy.getUsername(),proxy.getPassword());
                credentialsProvider.setCredentials(authScope, credentials);
                clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }else{
                clientBuilder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
            }
        }

        return clientBuilder.build();
    }

    private void generateCookie(HttpClientBuilder httpClientBuilder) {
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : config.getCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(config.getDomain());
            cookieStore.addCookie(cookie);
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }

}
