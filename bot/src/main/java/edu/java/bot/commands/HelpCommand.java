package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.UserService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final UserService userService;
    private final List<Command> commands;

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
        String commandsInfo = commands.stream()
                .filter(command -> !Objects.equals(command.command(), command()))
                .map(command -> String.format("%s - %s", command.command(), command.description()))
                .reduce("", (command1, command2) -> command1 + "\n" + command2);
        return new SendMessage(
                update.message().chat().id(),
                "*Available commands:*" + commandsInfo

        ).parseMode(ParseMode.Markdown);
    }
}
