package edu.java.bot.messageProcessors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProcessor implements UserMessageProcessor {
    private final List<Command> commands;

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        log.info(update.message().text());
        Command command = commands()
                .stream()
                .filter(cmd -> cmd.supports(update))
                .findFirst()
                .orElse(null);
        if (command == null) {
            return new SendMessage(
                    update.message().chat().id(),
                    "Please enter a right command or complete the input for the previous command"
            );
        } else {
            return command.handle(update);
        }
    }
}
