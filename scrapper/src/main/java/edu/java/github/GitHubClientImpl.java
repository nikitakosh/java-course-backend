package edu.java.github;

import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClientImpl implements GitHubClient {

    private final WebClient webClient;
    @Setter
    public String baseUrl = "https://api.github.com";

    public GitHubClientImpl(WebClient.Builder restClientBuilder) {
        this.webClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public RepoResponse fetchRepo(String owner, String repo) {
        return this.webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(RepoResponse.class)
                .block();
    }

}
