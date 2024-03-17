package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        scrapperClient.registerChat(chatId);
        return new SendMessage(
                chatId,
                "Congratulations, You are registered!"
        );
    }
}
