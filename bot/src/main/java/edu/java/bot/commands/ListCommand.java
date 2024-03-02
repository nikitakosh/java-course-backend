package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.models.Link;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final UserService userService;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show list of tracked links";
    }

    @Override
    public boolean supports(Update update) {
        Optional<User> user = userService.findByChatId(update.message().chat().id());
        return update.message().text().equals(command())
                && user.isPresent()
                && user.get().getState() == UserState.NEUTRAL;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        if (userService.findByChatId(chatId).isEmpty()) {
            return new SendMessage(
                    chatId,
                    "Please register! Use command /start"
            );
        }
        User user = userService.findByChatId(chatId).get();
        if (user.getLinks().isEmpty()) {
            return new SendMessage(
                    chatId,
                    "There are no tracked links"
            );
        } else {
            String urls = user.getLinks().stream()
                    .map(Link::getUrl)
                    .reduce("", (url1, url2) -> url1 + "\n" + url2);
            return new SendMessage(
                    chatId,
                    "Tracked Links: " + urls
            );
        }
    }


}
