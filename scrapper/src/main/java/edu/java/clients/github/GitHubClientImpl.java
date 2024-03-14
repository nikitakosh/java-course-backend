package edu.java.clients.github;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClientImpl implements GitHubClient {

    public static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public GitHubClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }


    @Override
    public RepoResponse fetchRepo(String owner, String repo) {
        return this.webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(RepoResponse.class)
                .block();
    }

    @Override
    public boolean isSupport(String url) {
        Pattern pattern = Pattern.compile("^https://github\\.com/[^/]+/[^/]+$");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}
