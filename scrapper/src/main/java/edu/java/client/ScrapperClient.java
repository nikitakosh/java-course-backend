package edu.java.client;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;

public interface ScrapperClient {
    void registerChat(Integer tgChatId);

    void deleteChat(Integer tgChatId);

    ListLinksResponse getLinks(Integer tgChatId);

    void addLink(Integer tgChatId, AddLinkRequest addLinkRequest);

    void deleteLink(Integer tgChatId, RemoveLinkRequest removeLinkRequest);
}
