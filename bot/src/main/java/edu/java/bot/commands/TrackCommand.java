package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controllers.dto.AddLinkRequest;
import edu.java.bot.controllers.dto.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start tracking link";
    }

    @Override
    public SendMessage handle(Update update) {
        String message = update.message().text();
        Long chatId = update.message().chat().id();
        try {
            scrapperClient.addLink(chatId, new AddLinkRequest(message.split(" ")[1]));
        } catch (WebClientResponseException ex) {
            return new SendMessage(
                    chatId,
                    ex.getResponseBodyAs(ApiErrorResponse.class).getDescription()
            );
        }
        return new SendMessage(
                chatId,
                "Link has started to be tracked"
        );
    }
}
