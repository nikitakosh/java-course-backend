package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final UserService userService;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register a user";
    }

    @Override
    public boolean supports(Update update) {
        Long chatId = update.message().chat().id();
        Optional<User> user = userService.findByChatId(chatId);
        if (user.isEmpty() || (update.message().text().equals(command())
                && user.get().getState() == UserState.NEUTRAL)) {
            return true;
        }
        return user.get().getState() == UserState.REGISTRATION;
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        Long chatId = update.message().chat().id();
        if (userService.findByChatId(chatId).isEmpty()) {
            User savingUser = new User();
            savingUser.setChatID(chatId);
            userService.save(
                    savingUser
            );
        }
        User user = userService.findByChatId(chatId).get();
        if (user.getState() == UserState.REGISTRATION) {
            user.setName(message);
            user.setState(UserState.NEUTRAL);
            userService.save(user);
            return new SendMessage(
                    chatId,
                    "Congratulations! You have registered"
            );
        } else {
            user.setState(UserState.REGISTRATION);
            userService.save(user);
            return new SendMessage(
                    chatId,
                    "Please, enter your name"
            );
        }
    }
}
