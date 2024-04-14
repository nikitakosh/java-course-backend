package edu.java.services.jooq;


import edu.java.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.domain.jooq.repositories.JooqTgChatRepository;
import edu.java.exceptions.ChatAlreadyExistException;
import edu.java.services.TgChatService;
import edu.java.services.dto.LinkDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {
    private final JooqTgChatRepository chatRepository;
    private final JooqChatLinkRepository chatLinkRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (chatRepository.find(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException("chat is already exist");
        }
        chatRepository.add(tgChatId);
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new ChatAlreadyExistException("chat is not exist");
        }
        chatLinkRepository.removeByChat(tgChatId);
        chatRepository.remove(tgChatId);
    }

    @Override
    @Transactional
    public List<Long> findChatsByLink(LinkDTO link) {
        return chatLinkRepository.findChatsByLink(link.getId());
    }
}
