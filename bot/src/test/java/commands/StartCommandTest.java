package commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class StartCommandTest {
    @Test
    public void registrationUser() {
        UserService userService = Mockito.mock(UserService.class);
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/start");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        User user = new User();
        user.setId(1L);
        user.setChatID(1L);
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        StartCommand startCommand = new StartCommand(userService);
        Assertions.assertEquals(
                startCommand.handle(update).getParameters().get("text"),
                "Please, enter your name"
        );
        Assertions.assertEquals(
                startCommand.handle(update).getParameters().get("text"),
                "Congratulations! You have registered"
        );
    }

    @Test
    public void commandAndDescription() {
        UserService userService = Mockito.mock(UserService.class);
        StartCommand startCommand = new StartCommand(userService);
        Assertions.assertEquals(
                startCommand.command(),
                "/start"
        );
        Assertions.assertEquals(
                startCommand.description(),
                "register a user"
        );
    }
}
