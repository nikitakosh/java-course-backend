package edu.java.clients.github;

public interface GitHubClient {
    RepoResponse fetchRepo(String owner, String repo);

    CommitResponse fetchCommit(String owner, String repo);

}
