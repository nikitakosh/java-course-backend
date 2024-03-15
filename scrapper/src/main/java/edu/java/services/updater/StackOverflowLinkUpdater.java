package edu.java.services.updater;

import edu.java.clients.client.BotClient;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.dto.AnswerItemResponse;
import edu.java.clients.stackoverflow.dto.QuestionItemResponse;
import edu.java.controllers.dto.LinkUpdate;
import edu.java.models.Link;
import edu.java.services.LinkService;
import edu.java.services.TgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StackOverflowLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final TgChatService chatService;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Override
    public void update(Link link) {
        URI uri = URI.create(link.getUrl());
        String[] path = uri.getPath().split("/");
        QuestionItemResponse itemResponse = stackOverflowClient.fetchQuestion(path[2]);
        if (link.getUpdatedAt() == null || itemResponse.lastActivityDate().isAfter(link.getUpdatedAt())) {
            AnswerItemResponse answerItemResponse = stackOverflowClient.fetchAnswer(path[2]);
            log.info(answerItemResponse.answerId().toString());
            log.info(answerItemResponse.owner().displayName());
            link.setUpdatedAt(itemResponse.lastActivityDate());
            link.setCreatedAt(OffsetDateTime.now());
            boolean isNewAnswer = false;
            if (!Objects.equals(link.getAnswerId(), answerItemResponse.answerId())) {
                link.setAnswerId(answerItemResponse.answerId());
                link.setAnswerOwner(answerItemResponse.owner().displayName());
                isNewAnswer = true;
            }
            linkService.update(link);
            botClient.sendMessage(
                    new LinkUpdate(
                            link.getUrl(),
                            String.format("link: %s is updated", link.getUrl()),
                            chatService.findChatsByLink(link),
                            isNewAnswer,
                            "Add new answer by %s".formatted(link.getAnswerOwner())
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
