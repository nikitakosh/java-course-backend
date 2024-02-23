package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.github.GitHubClient;
import edu.java.github.RepoResponse;
import edu.java.stackoverflow.ItemResponse;
import edu.java.stackoverflow.StackOverflowClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@Slf4j
public class ScrapperApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ScrapperApplication.class, args);
        StackOverflowClient stackOverflowClient = context.getBean(StackOverflowClient.class);
        GitHubClient gitHubClient = context.getBean(GitHubClient.class);
        RepoResponse repoResponse = gitHubClient.fetchRepo("nikitakosh", "TinkJavaCourse");
        ItemResponse itemResponse = stackOverflowClient.fetchQuestion("477816");
        log.info(itemResponse.toString());
        log.info(repoResponse.toString());
    }
}
