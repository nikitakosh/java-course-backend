package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.messageProcessors.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangeTrackerBot implements Bot {
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
    @PostConstruct
    public void start() {
        bot = new TelegramBot(config.telegramToken());
        bot.setUpdatesListener(this);
    }

    @Override
    @PreDestroy
    public void close() {
        bot.removeGetUpdatesListener();
        bot.shutdown();
    }

}
