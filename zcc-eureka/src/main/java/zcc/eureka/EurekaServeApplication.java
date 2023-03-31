package zcc.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableEurekaServer
public class EurekaServeApplication {
    static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext= SpringApplication.run(EurekaServeApplication.class,args);
    }
}
