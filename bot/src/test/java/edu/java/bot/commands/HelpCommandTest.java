package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HelpCommandTest {
    private static UserService userService;
    private static Update update;
    private static Message message;
    private static Chat chat;

    @BeforeAll
    public static void mockInit() {
        userService = Mockito.mock(UserService.class);
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
    }


    @Test
    public void helpMessage() {
        User user = new User();
        user.setState(UserState.NEUTRAL);
        Mockito.when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        HelpCommand helpCommand = new HelpCommand(userService);
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
        HelpCommand helpCommand = new HelpCommand(userService);
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
