package edu.java.services.jdbc;

import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcTgChatRepository;
import edu.java.exceptions.ChatAlreadyExistException;
import edu.java.models.Link;
import edu.java.services.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcTgChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.find(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException("chat is already exist");
        }
        chatRepository.add(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) {
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new ChatAlreadyExistException("chat is not exist");
        }
        chatRepository.remove(tgChatId);
    }

    @Override
    public List<Long> findChatsByLink(Link link) {
        return chatLinkRepository.findChatsByLink(link);
    }
}
