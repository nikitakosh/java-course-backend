package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "display a command window";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
                update.message().chat().id(),
                """
                        *Доступные команды:*
                                           
                        /start - Зарегистрировать пользователя. \s
                        /help - Вывести список команд. \s
                        /track - Начать отслеживание ссылки. \s
                        /untrack - Прекратить отслеживание ссылки. \s
                        /list - Показать список отслеживаемых ссылок.
                        """
        ).parseMode(ParseMode.Markdown);
    }
}
