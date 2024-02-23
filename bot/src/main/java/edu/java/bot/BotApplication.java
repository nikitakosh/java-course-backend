package edu.java.bot;

import edu.java.bot.client.BotClient;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controllers.dto.LinkUpdate;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BotApplication.class, args);
        Bot bot = context.getBean(ChangeTrackerBot.class);
        bot.start();
        BotClient botClient = context.getBean(BotClient.class);
        botClient.sendMessage(new LinkUpdate(1, "Url", "desc", List.of(1, 2, 3)));
    }
}
