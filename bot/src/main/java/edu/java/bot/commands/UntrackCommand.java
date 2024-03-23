package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controllers.dto.ApiErrorResponse;
import edu.java.bot.controllers.dto.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop tracking link";
    }


    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        Long chatId = update.message().chat().id();
        try {
            scrapperClient.deleteLink(chatId, new RemoveLinkRequest(message.split(" ")[1]));
        } catch (WebClientResponseException ex) {
            return new SendMessage(
                    chatId,
                    ex.getResponseBodyAs(ApiErrorResponse.class).getDescription()
            );
        }
        return new SendMessage(
                chatId,
                "Link is no longer tracked"
        );
    }
}
