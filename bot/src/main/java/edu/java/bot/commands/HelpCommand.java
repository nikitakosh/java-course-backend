package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final UserService userService;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "display a command window";
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
        return new SendMessage(
                update.message().chat().id(),
                """
                        *Доступные команды:*
                        /start - Зарегистрировать пользователя. \s
                        /help - Вывести список команд. \s
                        /track - Начать отслеживание ссылки. \s
                        /untrack - Прекратить отслеживание ссылки. \s
                        /list - Показать список отслеживаемых ссылок.
                        """
        ).parseMode(ParseMode.Markdown);
    }
}
