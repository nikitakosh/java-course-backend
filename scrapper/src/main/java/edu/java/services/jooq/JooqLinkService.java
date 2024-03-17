package edu.java.services.jooq;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.domain.jooq.repositories.JooqLinkRepository;
import edu.java.domain.jooq.repositories.JooqTgChatRepository;
import edu.java.domain.jooq.tables.pojos.Link;
import edu.java.exceptions.NotExistentChatException;
import edu.java.exceptions.NotExistentLinkException;
import edu.java.services.LinkService;
import edu.java.services.dto.LinkDTO;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class JooqLinkService implements LinkService {
    public static final String CHAT_IS_NOT_EXIST = "chat is not exist";
    private final JooqLinkRepository linkRepository;
    private final JooqTgChatRepository chatRepository;
    private final JooqChatLinkRepository chatLinkRepository;

    @Override
    public void add(long tgChatId, AddLinkRequest addLinkRequest) {
        String url = addLinkRequest.getLink();
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        Optional<Link> linkOptional = linkRepository.find(url);
        if (linkOptional.isEmpty()) {
            Link link = new Link();
            link.setUrl(url);
            link.setCreatedAt(OffsetDateTime.now());
            int linkId = linkRepository.add(link);
            log.info(String.valueOf(linkId));
            chatLinkRepository.add(linkId, tgChatId);
        } else {
            if (chatLinkRepository.find(tgChatId, linkOptional.get().getId()).isEmpty()) {
                chatLinkRepository.add(linkOptional.get().getId(), tgChatId);
            }
        }
    }

    @Override
    public void remove(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        String url = removeLinkRequest.getLink();
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        Optional<Link> linkOptional = linkRepository.find(url);
        if (linkOptional.isEmpty()) {
            throw new NotExistentLinkException("link is nox exist");
        }
        chatLinkRepository.remove(tgChatId, linkOptional.get());
        if (!chatLinkRepository.isLinkPresent(linkOptional.get().getId())) {
            linkRepository.remove(url);
        }
    }

    @Override
    public void update(LinkDTO link) {
        linkRepository.update(new Link(link.getId(),
                link.getUrl(),
                link.getUpdatedAt(),
                link.getCreatedAt(),
                link.getCommitMessage(),
                link.getCommitSHA(),
                link.getAnswerId(),
                link.getAnswerOwner()));
    }

    @Override
    public List<LinkDTO> findOldLinks(long secondsThreshold) {
        return linkRepository.findAll()
                .stream()
                .filter(
                        link -> ChronoUnit.SECONDS.between(
                                link.getCreatedAt(), OffsetDateTime.now()
                        ) >= secondsThreshold
                )
                .map(link -> new LinkDTO(
                        link.getId(),
                        link.getUrl(),
                        link.getUpdatedAt(),
                        link.getCreatedAt(),
                        link.getCommitMessage(),
                        link.getCommitSha(),
                        link.getAnswerId(),
                        link.getAnswerOwner()))
                .toList();
    }

    @Override
    public List<LinkDTO> listAll(long tgChatId) {
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        return linkRepository.findAllByChat(tgChatId).stream()
                .map(link -> new LinkDTO(
                        link.getId(),
                        link.getUrl(),
                        link.getUpdatedAt(),
                        link.getCreatedAt(),
                        link.getCommitMessage(),
                        link.getCommitSha(),
                        link.getAnswerId(),
                        link.getAnswerOwner()))
                .toList();
    }
}
