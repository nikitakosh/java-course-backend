package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        Pattern pattern = Pattern.compile("^%s$".formatted(command()));
        Matcher matcher = pattern.matcher(update.message().text());
        return matcher.matches();
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
