package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.clients.ClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ClientConfiguration.class})
@Slf4j
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
