package edu.java.clients.github;

import edu.java.configuration.clients.GitHubClientConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClientImpl implements GitHubClient {

    private final WebClient webClient;
    private final GitHubClientConfiguration clientConfiguration;

    public GitHubClientImpl(GitHubClientConfiguration clientConfiguration, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(clientConfiguration.baseUrl()).build();
        this.clientConfiguration = clientConfiguration;
    }

    public GitHubClientImpl(GitHubClientConfiguration clientConfiguration,
                            WebClient.Builder webClientBuilder,
                            String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.clientConfiguration = clientConfiguration;
    }


    @Override
    public RepoResponse fetchRepo(String owner, String repo) {
        return this.webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(RepoResponse.class)
                .retryWhen(clientConfiguration.getRetry())
                .block();
    }

    @Override
    public CommitResponse fetchCommit(String owner, String repo) {
        return this.webClient.get()
                .uri("/repos/{owner}/{repo}/commits", owner, repo)
                .retrieve()
                .toEntityList(CommitResponse.class)
                .retryWhen(clientConfiguration.getRetry())
                .block()
                .getBody()
                .getFirst();
    }


}
