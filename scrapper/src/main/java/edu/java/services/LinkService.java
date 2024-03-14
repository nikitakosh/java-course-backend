package edu.java.services;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.models.Link;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, AddLinkRequest addLinkRequest);

    Link remove(long tgChatId, RemoveLinkRequest removeLinkRequest);

    void update(Link link);

    List<Link> findOldLinks(long secondsThreshold);

    ListLinksResponse listAll(long tgChatId);
}
