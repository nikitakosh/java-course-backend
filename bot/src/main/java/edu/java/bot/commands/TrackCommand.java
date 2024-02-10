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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackCommand.class);
    private final UserService userService;
    private final LinkService linkService;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start tracking link";
    }

    @Override
    public boolean supports(Update update) {
        if (update.message().text().equals(command())) {
            return true;
        }
        Long chatId = update.message().chat().id();
        return userService.findByChatId(chatId).isPresent()
                && userService.findByChatId(chatId).get().getState() == UserState.TRACK;
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
        if (user.getState() == UserState.TRACK) {
            if (linkService.findByUrl(message).isEmpty()) {
                Link link = new Link();
                link.setUrl(message);
                linkService.save(link);
            }
            Link link = linkService.findByUrl(message).get();
            user.setState(UserState.NEUTRAL);
            userService.save(user);
            userService.trackLink(user, link);
            return new SendMessage(
                    chatId,
                    "Link is now tracked!"
            );
        } else {
            user.setState(UserState.TRACK);
            userService.save(user);
            return new SendMessage(
                    chatId,
                    "Please, enter the link you want to track"
            );
        }
    }
}
