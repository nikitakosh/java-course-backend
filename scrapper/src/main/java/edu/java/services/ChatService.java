package edu.java.services;

import edu.java.exceptions.ChatAlreadyExistException;
import edu.java.exceptions.NotExistentChatException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final List<Integer> chats = new ArrayList<>();
    private final LinkService linkService;

    public void registerChat(Integer tgChatId) {
        if (chats.contains(tgChatId)) {
            throw new ChatAlreadyExistException("chat already exist");
        }
        chats.add(tgChatId);
        linkService.putChat(tgChatId);
    }

    public void removeChat(Integer tgChatId) {
        if (!chats.contains(tgChatId)) {
            throw new NotExistentChatException("chat does not exist");
        }
        chats.remove(tgChatId);
    }
}
