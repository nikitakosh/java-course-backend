package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageProcessor implements UserMessageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
    private final HelpCommand helpCommand;
    private final ListCommand listCommand;
    private final StartCommand startCommand;
    private final TrackCommand trackCommand;
    private final UntrackCommand untrackCommand;

    @Override
    public List<? extends Command> commands() {
        return List.of(
                helpCommand,
                listCommand,
                startCommand,
                trackCommand,
                untrackCommand
        );
    }

    @Override
    public SendMessage process(Update update) {
        List<? extends Command> rightCommand = commands()
                .stream()
                .filter(command -> command.supports(update))
                .toList();
        if (rightCommand.size() != 1) {
            return new SendMessage(
                    update.message().chat().id(),
                    "Please enter a right command or choose from menu"
            );
        } else {
            return rightCommand.getFirst().handle(update);
        }
    }
}
