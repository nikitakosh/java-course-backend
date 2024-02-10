package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command;
import edu.java.bot.models.Link;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.LinkService;
import edu.java.bot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private final UserService userService;
    private final LinkService linkService;
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking link";
    }

    @Override
    public boolean supports(Update update) {
        if (update.message().text().equals(command())) {
            return true;
        }
        Long chatId = update.message().chat().id();
        return userService.findByChatId(chatId).isPresent() &&
                userService.findByChatId(chatId).get().getState() == UserState.UNTRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        Long chatId = update.message().chat().id();
        if (userService.findByChatId(chatId).isEmpty()) {
            return new SendMessage(
                    chatId,
                    "Please register! Use command /start"
            );
        }
        User user = userService.findByChatId(chatId).get();
        if (user.getState() == UserState.UNTRACK) {
            Optional<Link> link = linkService.findByUrl(message);
            if (link.isEmpty() ||
                    !userService.wasLinkTracked(user, link.get())) {
                user.setState(UserState.NEUTRAL);
                userService.save(user);
                return new SendMessage(
                        chatId,
                        "This link was not tracked"
                );
            }

            user.getLinks().removeIf(l -> Objects.equals(l.getUrl(), link.get().getUrl()));
            user.setState(UserState.NEUTRAL);
            userService.save(user);
            return new SendMessage(
                    chatId,
                    "The link is no longer tracked"
            );
        } else {
            user.setState(UserState.UNTRACK);
            userService.save(user);
            return new SendMessage(
                    chatId,
                    "Please, enter the link you want to unfollow"
            );
        }
    }
}
