package edu.java.bot.client;


import edu.java.bot.controllers.dto.AddLinkRequest;
import edu.java.bot.controllers.dto.ListLinksResponse;
import edu.java.bot.controllers.dto.RemoveLinkRequest;

public interface ScrapperClient {
    void registerChat(Integer tgChatId);

    void deleteChat(Integer tgChatId);

    ListLinksResponse getLinks(Integer tgChatId);

    void addLink(Integer tgChatId, AddLinkRequest addLinkRequest);

    void deleteLink(Integer tgChatId, RemoveLinkRequest removeLinkRequest);
}
