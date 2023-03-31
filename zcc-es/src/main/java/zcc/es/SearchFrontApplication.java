package zcc.es;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;



@SpringBootApplication
@EnableEurekaClient
public class SearchFrontApplication {
    static ConfigurableApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(SearchFrontApplication.class, args);
    }


}
