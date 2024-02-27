package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.messageProcessors.UserMessageProcessor;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeTrackerBot implements Bot {

    private final ApplicationConfig config;
    private final UserMessageProcessor messageProcessor;
    private final List<Command> commands;
    private TelegramBot bot;


    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> execute(messageProcessor.process(update)));
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        SetMyCommands setMyCommands = new SetMyCommands(
                commands.stream().map(Command::toApiCommand).toArray(BotCommand[]::new)
        );
        bot = new TelegramBot(config.telegramToken());
        bot.execute(setMyCommands);
        bot.setUpdatesListener(this);
    }

    @Override
    @PreDestroy
    public void close() {
        bot.removeGetUpdatesListener();
        bot.shutdown();
    }

}
