package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controllers.dto.LinkResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show list of tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        List<String> urls = scrapperClient.getLinks(chatId)
                .getLinks().stream()
                .map(LinkResponse::getUrl)
                .toList();
        return new SendMessage(
                chatId,
                "Tracked links:"
                        + urls.stream().reduce("\n", (url1, url2) -> url1 + "\n" + url2)
        );
    }


}
