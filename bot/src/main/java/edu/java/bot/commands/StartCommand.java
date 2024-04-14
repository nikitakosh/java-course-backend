package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controllers.dto.ApiErrorResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register a user";
    }


    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        try {
            scrapperClient.registerChat(chatId);
        } catch (WebClientResponseException ex) {
            return new SendMessage(
                    chatId,
                    Objects.requireNonNull(ex.getResponseBodyAs(ApiErrorResponse.class)).getDescription()
            );
        }
        return new SendMessage(
                chatId,
                "Congratulations, You are registered!"
        );
    }
}
