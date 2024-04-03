package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.ScrapperClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ScrapperClientConfiguration.class})
public class BotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
