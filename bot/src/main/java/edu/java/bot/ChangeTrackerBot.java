package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChangeTrackerBot implements Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeTrackerBot.class);
    private final ApplicationConfig config;
    private final UserMessageProcessor messageProcessor;
    private TelegramBot bot;

    @Autowired
    public ChangeTrackerBot(ApplicationConfig config, UserMessageProcessor messageProcessor) {
        this.config = config;
        this.messageProcessor = messageProcessor;
    }


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
        bot = new TelegramBot(config.telegramToken());
        bot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
    }

}
