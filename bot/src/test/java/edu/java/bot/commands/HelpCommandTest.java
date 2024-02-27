package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HelpCommandTest {

    private final List<Command> commands;
    @Mock
    private UserService userService;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @Autowired
    public HelpCommandTest(List<Command> commands) {
        this.commands = commands;
    }


    @Test
    public void helpMessage() {
        User user = new User();
        user.setState(UserState.NEUTRAL);
        Mockito.when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);

        HelpCommand helpCommand = new HelpCommand(userService, commands);
        Assertions.assertEquals(
                helpCommand.handle(update).getParameters().get("text"),
                """
                        *Available commands:*
                        /list - show list of tracked links
                        /start - register a user
                        /track - start tracking link
                        /untrack - stop tracking link"""
        );
    }


    @Test
    public void commandAndDescription() {
        HelpCommand helpCommand = new HelpCommand(userService, commands);
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
