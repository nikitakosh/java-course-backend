package edu.java.github;

public interface GitHubClient {
    RepoResponse fetchRepo(String owner, String repo);
}
