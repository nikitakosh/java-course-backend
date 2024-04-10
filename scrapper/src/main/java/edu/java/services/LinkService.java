package edu.java.services;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.services.dto.LinkDTO;
import java.util.List;

public interface LinkService {
    void add(long tgChatId, AddLinkRequest addLinkRequest);

    void remove(long tgChatId, RemoveLinkRequest removeLinkRequest);

    void update(LinkDTO link);

    List<LinkDTO> findOldLinks(long secondsThreshold);

    List<LinkDTO> listAll(long tgChatId);
}
