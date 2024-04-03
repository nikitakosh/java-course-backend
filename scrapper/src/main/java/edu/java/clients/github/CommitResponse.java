package edu.java.clients.github;

public record CommitResponse(
        String sha,
        CommitInfo commit
) {

}
