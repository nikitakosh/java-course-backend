package edu.java.clients.github;

public interface GitHubClient {
    RepoResponse fetchRepo(String owner, String repo);

    boolean isSupport(String url);
}
