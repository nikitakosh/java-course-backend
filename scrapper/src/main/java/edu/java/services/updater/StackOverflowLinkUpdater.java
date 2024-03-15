package edu.java.services.updater;

import edu.java.clients.client.BotClient;
import edu.java.clients.stackoverflow.ItemResponse;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.controllers.dto.LinkUpdate;
import edu.java.models.Link;
import edu.java.services.LinkService;
import edu.java.services.TgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final TgChatService chatService;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Override
    public void update(Link link) {
        URI uri = URI.create(link.getUrl());
        String[] path = uri.getPath().split("/");
        ItemResponse itemResponse = stackOverflowClient.fetchQuestion(path[2]);
        if (link.getUpdatedAt() == null || itemResponse.lastActivityDate().isAfter(link.getUpdatedAt())) {
            link.setUpdatedAt(itemResponse.lastActivityDate());
            link.setCreatedAt(OffsetDateTime.now());
            linkService.update(link);
            botClient.sendMessage(
                    new LinkUpdate(
                            link.getUrl(),
                            String.format("link: %s is updated", link.getUrl()),
                            chatService.findChatsByLink(link)
                    )
            );
        } else {
            link.setCreatedAt(OffsetDateTime.now());
            linkService.update(link);
        }
    }

    @Override
    public boolean supports(URI link) {
        return link.getHost().equals("stackoverflow.com");
    }
}
