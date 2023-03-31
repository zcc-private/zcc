package zcc.es.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ESClientSpringFactory {
    private final Logger LOGGER = LoggerFactory.getLogger(ESClientSpringFactory.class);
    public static int CONNECT_TIMEOUT_MILLIS = 1000;
    public static int SOCKET_TIMEOUT_MILLIS = 30000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;
    public static int MAX_CONN_PER_ROUTE = 10;
    public static int MAX_CONN_TOTAL = 30;
    public static String USER_AGENT = "";
    public static String USER_NAME = "";
    public static String PASSWORD = "";
    private static HttpHost[] HTTP_HOST;
    private RestClientBuilder builder;
    private RestClient restClient;
    private RestHighLevelClient restHighLevelClient;
    private static ESClientSpringFactory esClientSpringFactory = new ESClientSpringFactory();
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    public ESClientSpringFactory() {
    }

    public static ESClientSpringFactory build(EsConfig config) {
        HTTP_HOST = config.httpHost();
        CONNECT_TIMEOUT_MILLIS = config.getConnectTimeout();
        SOCKET_TIMEOUT_MILLIS = config.getSocketTimeout();
        CONNECTION_REQUEST_TIMEOUT_MILLIS = config.getRequestTimeout();
        MAX_CONN_TOTAL = config.getMaxConnTotal();
        MAX_CONN_PER_ROUTE = config.getMaxConnPerRoute();
        USER_AGENT = config.getUserAgent();
        return esClientSpringFactory;
    }

    public void init() {
        this.builder = RestClient.builder(HTTP_HOST);
        this.setCredentials(this.builder);
        this.setUserAgent();
        this.setConnectTimeOutConfig();
        this.setMutiConnectConfig();
        this.restClient = this.builder.build();
        this.restHighLevelClient = new RestHighLevelClient(this.builder);
    }

    private void setCredentials(RestClientBuilder builder) {
        if (!StringUtils.isEmpty(USER_NAME)) {
            this.credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USER_NAME, PASSWORD));
            builder.setHttpClientConfigCallback((httpClientBuilder) -> {
                return httpClientBuilder.setDefaultCredentialsProvider(this.credentialsProvider);
            });
        }
    }

    public void setUserAgent() {
        if (!StringUtils.isEmpty(USER_AGENT)) {
            BasicHeader[] basicHeaders = new BasicHeader[]{new BasicHeader("Accept", "application/json; charset=UTF-8"), new BasicHeader("User-Agent", USER_AGENT)};
            this.builder.setDefaultHeaders(basicHeaders);
        }
    }

    public void setConnectTimeOutConfig() {
        this.builder.setRequestConfigCallback((requestConfigBuilder) -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });
    }

    public void setMutiConnectConfig() {
        this.builder.setHttpClientConfigCallback((httpClientBuilder) -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            return httpClientBuilder;
        });
    }

    public RestClient getClient() {
        return this.restClient;
    }

    public RestHighLevelClient getRhlClient() {
        return this.restHighLevelClient;
    }

    public void close() {
        if (this.restClient != null) {
            try {
                this.restClient.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

        this.LOGGER.info("close client");
    }
}
