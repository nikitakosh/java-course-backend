package edu.java.bot.services;

import edu.java.bot.models.Link;
import edu.java.bot.models.User;
import edu.java.bot.repositories.UserRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LinkService linkService;

    @Transactional(readOnly = true)
    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatID(chatId);
    }

    @Transactional(readOnly = true)
    public boolean wasLinkTracked(User user, Link link) {
        return user.getLinks().stream()
                .anyMatch(userLink -> Objects.equals(userLink.getUrl(), link.getUrl()));
    }


    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void trackLink(User user, Link link) {
        user.getLinks().add(link);
        link.getUsers().add(user);
        save(user);
        linkService.save(link);
    }


}
