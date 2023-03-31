package zcc.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.slf4j.LoggerFactory;

@Configuration
@ComponentScan(
        basePackageClasses = {ESClientSpringFactory.class}
)
public class EsConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(EsConfig.class);
    @Value("${elasticSearch.client.connectNum}")
    private Integer connectNum;
    @Value("${elasticSearch.client.connectPerRoute}")
    private Integer connectPerRoute;
    @Value("${elasticSearch.hostlist}")
    private String hostlist;
    @Value("${elasticSearch.client.connect.timeout:1000}")
    private int connectTimeout;
    @Value("${elasticSearch.client.socket.timeout:30000}")
    private int socketTimeout;
    @Value("${elasticSearch.client.request.timeout:30000}")
    private int requestTimeout;
    @Value("${elasticSearch.client.max_conn_per_route:10}")
    private int maxConnPerRoute;
    @Value("${elasticSearch.client.max_conn_total:30}")
    private int maxConnTotal;
    @Value("${elasticSearch.client.user-agent}")
    private String userAgent;
    @Value("${elasticsearch.username}")
    private String userName;
    @Value("${elasticsearch.password}")
    private String password;

    public EsConfig() {
    }

    @Bean
    public HttpHost[] httpHost() {
        String[] split = this.hostlist.split(",");
        HttpHost[] httpHostArray = new HttpHost[split.length];

        for(int i = 0; i < split.length; ++i) {
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }

        this.LOGGER.info("init HttpHost");
        return httpHostArray;
    }

    @Bean(
            initMethod = "init",
            destroyMethod = "close"
    )
    public ESClientSpringFactory getFactory() {
        this.LOGGER.info("ESClientSpringFactory 初始化");
        return ESClientSpringFactory.build(this);
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient() {
        this.LOGGER.info("RestClient 初始化");
        return this.getFactory().getClient();
    }

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient() {
        this.LOGGER.info("RestHighLevelClient 初始化");
        return this.getFactory().getRhlClient();
    }

    public Logger getLOGGER() {
        return this.LOGGER;
    }

    public Integer getConnectNum() {
        return this.connectNum;
    }

    public Integer getConnectPerRoute() {
        return this.connectPerRoute;
    }

    public String getHostlist() {
        return this.hostlist;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    public int getRequestTimeout() {
        return this.requestTimeout;
    }

    public int getMaxConnPerRoute() {
        return this.maxConnPerRoute;
    }

    public int getMaxConnTotal() {
        return this.maxConnTotal;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setConnectNum(Integer connectNum) {
        this.connectNum = connectNum;
    }

    public void setConnectPerRoute(Integer connectPerRoute) {
        this.connectPerRoute = connectPerRoute;
    }

    public void setHostlist(String hostlist) {
        this.hostlist = hostlist;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setMaxConnPerRoute(int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    public void setMaxConnTotal(int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
