package edu.java.services.jdbc;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.LinkResponse;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcLinkDao;
import edu.java.domain.jdbc.JdbcTgChatRepository;
import edu.java.exceptions.NotExistentChatException;
import edu.java.exceptions.NotExistentLinkException;
import edu.java.models.Link;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkService implements LinkService {
    public static final String CHAT_IS_NOT_EXIST = "chat is not exist";
    private final JdbcLinkDao linkRepository;
    private final JdbcTgChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    @Override
    @Transactional
    public Link add(long tgChatId, AddLinkRequest addLinkRequest) {
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
            return link;
        } else {
            if (chatLinkRepository.find(linkOptional.get().getId(), tgChatId).isEmpty()) {
                chatLinkRepository.add(linkOptional.get().getId(), tgChatId);
            }
            return linkOptional.get();
        }
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        String url = removeLinkRequest.getLink();
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        Optional<Link> linkOptional = linkRepository.find(url);
        if (linkOptional.isEmpty()) {
            throw new NotExistentLinkException("link is nox exist");
        }
        chatLinkRepository.remove(tgChatId, linkOptional.get());
        if (!chatLinkRepository.isLinkPresent(linkOptional.get())) {
            linkRepository.remove(url);
        }
        return linkOptional.get();
    }

    @Override
    public void update(Link link) {
        linkRepository.update(link);
    }

    @Override
    public List<Link> findOldLinks(long secondsThreshold) {
        return linkRepository.findAll()
                .stream()
                .filter(
                        link -> ChronoUnit.SECONDS.between(
                                link.getCreatedAt(), OffsetDateTime.now()
                        ) >= secondsThreshold
                )
                .toList();
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        List<LinkResponse> links = linkRepository.findAllByChat(tgChatId).stream()
                .map(link -> new LinkResponse(link.getId(), link.getUrl()))
                .toList();
        return new ListLinksResponse(
                links,
                links.size()
        );
    }
}
