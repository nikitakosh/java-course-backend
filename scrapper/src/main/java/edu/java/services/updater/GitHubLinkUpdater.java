package edu.java.services.updater;

import edu.java.clients.github.CommitResponse;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.RepoResponse;
import edu.java.controllers.dto.LinkUpdate;
import edu.java.services.LinkService;
import edu.java.services.SenderService;
import edu.java.services.TgChatService;
import edu.java.services.dto.LinkDTO;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubLinkUpdater implements LinkUpdater {

    private final LinkService linkService;
    private final TgChatService chatService;
    private final GitHubClient gitHubClient;
    private final SenderService senderService;


    @Override
    public void update(LinkDTO link) {
        URI uri = URI.create(link.getUrl());
        String[] path = uri.getPath().split("/");
        RepoResponse repoResponse = gitHubClient.fetchRepo(path[1], path[2]);
        if (link.getUpdatedAt() == null
                || repoResponse.updatedAt().isAfter(link.getUpdatedAt())) {
            CommitResponse commitResponse = gitHubClient.fetchCommit(path[1], path[2]);
            link.setUpdatedAt(repoResponse.updatedAt());
            link.setCreatedAt(OffsetDateTime.now());
            boolean isCommitUpdate = false;
            if (!Objects.equals(link.getCommitSHA(), commitResponse.sha())) {
                link.setCommitSHA(commitResponse.sha());
                link.setCommitMessage(commitResponse.commit().message());
                isCommitUpdate = true;
            }
            linkService.update(link);
            senderService.send(
                    new LinkUpdate(
                            link.getUrl(),
                            "link: %s is updated".formatted(link.getUrl()),
                            chatService.findChatsByLink(link),
                            isCommitUpdate,
                            "Commit message: %s".formatted(link.getCommitMessage())
                    )
            );
        } else {
            link.setCreatedAt(OffsetDateTime.now());
            linkService.update(link);
        }
    }

    @Override
    public boolean supports(URI link) {
        return link.getHost().equals("github.com");
    }
}
