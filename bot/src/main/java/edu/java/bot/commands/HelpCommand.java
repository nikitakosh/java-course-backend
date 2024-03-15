package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

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
