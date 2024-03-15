package edu.java.bot.client;


import edu.java.bot.controllers.dto.AddLinkRequest;
import edu.java.bot.controllers.dto.ListLinksResponse;
import edu.java.bot.controllers.dto.RemoveLinkRequest;

public interface ScrapperClient {
    void registerChat(Long tgChatId);

    void deleteChat(Long tgChatId);

    ListLinksResponse getLinks(Long tgChatId);

    void addLink(Long tgChatId, AddLinkRequest addLinkRequest);

    void deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest);
}
