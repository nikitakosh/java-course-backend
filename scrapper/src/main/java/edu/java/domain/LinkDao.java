package edu.java.domain;

import edu.java.models.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;


public interface LinkDao {
    void add(Long tgChatId, Link link);
    void remove(Long tgChatId, String url);
    List<Link> findAll(Long tgChatId);
    Optional<Link> findLink(String url);
}
