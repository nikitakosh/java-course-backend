package edu.java.bot.commands;


import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.User;
import edu.java.bot.services.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StartCommandTest {
    @Mock
    private UserService userService;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @Test
    public void registrationUser() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/start");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        User user = new User();
        user.setId(1L);
        user.setChatID(1L);
        StartCommand startCommand = new StartCommand(userService);
        Mockito.when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
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
