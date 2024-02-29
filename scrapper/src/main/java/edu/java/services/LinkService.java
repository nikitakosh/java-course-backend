package edu.java.services;

import edu.java.exceptions.LinkAlreadyTrackException;
import edu.java.exceptions.LinkWasNotTrackedException;
import edu.java.exceptions.NotExistentChatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkService {
    public static final String CHAT_DOES_NOT_EXIST = "chat does not exist";
    private final Map<Integer, List<String>> links = new HashMap<>();

    public List<String> getLinks(Integer tgChatId) {
        if (!links.containsKey(tgChatId)) {
            throw new NotExistentChatException(CHAT_DOES_NOT_EXIST);
        }
        return links.get(tgChatId);
    }

    public void putChat(Integer tgChatId) {
        log.info("putChat");
        links.put(tgChatId, new ArrayList<>());
    }

    public void addLink(Integer tgChatId, String url) {
        if (!links.containsKey(tgChatId)) {
            throw new NotExistentChatException(CHAT_DOES_NOT_EXIST);
        }
        if (links.get(tgChatId).contains(url)) {
            throw new LinkAlreadyTrackException("link already tracked");
        }
        links.get(tgChatId).add(url);
    }

    public void removeLink(Integer tgChatId, String url) {
        if (links.containsKey(tgChatId)) {
            throw new NotExistentChatException(CHAT_DOES_NOT_EXIST);
        }
        if (!links.get(tgChatId).contains(url)) {
            throw new LinkWasNotTrackedException("link was not tracked");
        }
        links.get(tgChatId).remove(url);
    }
}
