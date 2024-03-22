package edu.java.services.jpa;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.domain.jpa.entity.ChatEntity;
import edu.java.domain.jpa.entity.LinkEntity;
import edu.java.domain.jpa.repositoires.JpaLinkRepository;
import edu.java.domain.jpa.repositoires.JpaTgChatRepository;
import edu.java.exceptions.LinkWasNotTrackedException;
import edu.java.exceptions.NotExistentChatException;
import edu.java.exceptions.NotExistentLinkException;
import edu.java.services.LinkService;
import edu.java.services.dto.LinkDTO;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    public static final String CHAT_IS_NOT_EXIST = "chat is not exist";
    public static final String LINK_IS_NOT_EXIST = "link is not exist";
    private final JpaLinkRepository linkRepository;
    private final JpaTgChatRepository chatRepository;

    @Override
    @Transactional
    public void add(long tgChatId, AddLinkRequest addLinkRequest) {
        String url = addLinkRequest.getLink();
        Optional<ChatEntity> chat = chatRepository.findById(tgChatId);
        if (chat.isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        Optional<LinkEntity> linkOptional = linkRepository.findByUrl(url);
        if (linkOptional.isEmpty()) {
            LinkEntity link = new LinkEntity();
            link.setUrl(url);
            link.setCreatedAt(OffsetDateTime.now());
            link.addChat(chat.get());
            linkRepository.save(link);
        } else {
            linkOptional.get().addChat(chat.get());
            linkRepository.save(linkOptional.get());
        }
    }

    @Override
    @Transactional
    public void remove(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        String url = removeLinkRequest.getLink();
        Optional<ChatEntity> chat = chatRepository.findById(tgChatId);
        if (chat.isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        Optional<LinkEntity> linkOptional = linkRepository.findByUrl(url);
        if (linkOptional.isEmpty()) {
            throw new NotExistentLinkException(LINK_IS_NOT_EXIST);
        }
        if (!chat.get().getLinks().contains(linkOptional.get())) {
            throw new LinkWasNotTrackedException("link was not tracked");
        }
        linkOptional.get().removeChat(chat.get());
        if (linkOptional.get().getChats().isEmpty()) {
            linkRepository.delete(linkOptional.get());
        } else {
            linkRepository.save(linkOptional.get());
        }
    }

    @Override
    @Transactional
    public void update(LinkDTO link) {
        Optional<LinkEntity> optionalLink = linkRepository.findByUrl(link.getUrl());
        if (optionalLink.isEmpty()) {
            throw new NotExistentLinkException(LINK_IS_NOT_EXIST);
        }
        optionalLink.get().setUpdatedAt(link.getUpdatedAt());
        optionalLink.get().setCreatedAt(link.getCreatedAt());
        optionalLink.get().setCommitMessage(link.getCommitMessage());
        optionalLink.get().setCommitSha(link.getCommitSHA());
        optionalLink.get().setAnswerId(link.getAnswerId());
        optionalLink.get().setAnswerOwner(link.getAnswerOwner());
        linkRepository.save(optionalLink.get());
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<LinkDTO> listAll(long tgChatId) {
        Optional<ChatEntity> chat = chatRepository.findById(tgChatId);
        if (chat.isEmpty()) {
            throw new NotExistentChatException(CHAT_IS_NOT_EXIST);
        }
        return chat.get().getLinks().stream()
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
