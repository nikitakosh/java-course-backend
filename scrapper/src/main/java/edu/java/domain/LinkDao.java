package edu.java.domain;

import edu.java.models.Link;
import java.util.List;
import java.util.Optional;


public interface LinkDao {
    Integer add(Link link);

    Integer remove(String url);

    void update(Link link);

    List<Link> findAll();

    List<Link> findAllByChat(Long tgChatId);

    Optional<Link> find(String url);
}
