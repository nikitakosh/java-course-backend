package edu.java.services.jpa;

import edu.java.domain.jpa.entity.ChatEntity;
import edu.java.domain.jpa.entity.LinkEntity;
import edu.java.domain.jpa.repositoires.JpaLinkRepository;
import edu.java.domain.jpa.repositoires.JpaTgChatRepository;
import edu.java.exceptions.ChatAlreadyExistException;
import edu.java.exceptions.NotExistentLinkException;
import edu.java.services.TgChatService;
import edu.java.services.dto.LinkDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaLinkRepository linkRepository;
    private final JpaTgChatRepository chatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException("chat is already exist");
        }
        ChatEntity chat = new ChatEntity();
        chat.setId(tgChatId);
        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        Optional<ChatEntity> chat = chatRepository.findById(tgChatId);
        if (chat.isEmpty()) {
            throw new ChatAlreadyExistException("chat is not exist");
        }
        for (LinkEntity link : chat.get().getLinks()) {
            if (link.getChats().size() == 1) {
                linkRepository.delete(link);
            }
        }
        chatRepository.deleteById(tgChatId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findChatsByLink(LinkDTO link) {
        Optional<LinkEntity> linkEntity = linkRepository.findById(link.getId());
        if (linkEntity.isEmpty()) {
            throw new NotExistentLinkException("link is not exist");
        }
        return linkEntity.get().getChats().stream().map(ChatEntity::getId).toList();
    }
}
