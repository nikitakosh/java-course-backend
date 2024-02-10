package commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.HelpCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HelpCommandTest {


    @Test
    public void helpMessage() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        HelpCommand helpCommand = new HelpCommand();
        Assertions.assertEquals(
                helpCommand.handle(update).getParameters().get("text"),
                """
                        *Доступные команды:*
                                           
                        /start - Зарегистрировать пользователя. \s
                        /help - Вывести список команд. \s
                        /track - Начать отслеживание ссылки. \s
                        /untrack - Прекратить отслеживание ссылки. \s
                        /list - Показать список отслеживаемых ссылок.
                        """
        );
    }


    @Test
    public void commandAndDescription() {
        HelpCommand helpCommand = new HelpCommand();
        Assertions.assertEquals(
                helpCommand.command(),
                "/help"
        );
        Assertions.assertEquals(
                helpCommand.description(),
                "display a command window"
        );
    }

}
