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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HelpCommandTest {

    @Mock
    private ListCommand listCommand;
    @Mock
    private StartCommand startCommand;
    @Mock
    private TrackCommand trackCommand;
    @Mock
    private UntrackCommand untrackCommand;
    @Mock
    private UserService userService;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;


    @Test
    public void helpMessage() {
        User user = new User();
        user.setState(UserState.NEUTRAL);
        Mockito.when(userService.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        Mockito.when(listCommand.command()).thenReturn("/list");
        Mockito.when(listCommand.description()).thenReturn("show list of tracked links");
        Mockito.when(startCommand.command()).thenReturn("/start");
        Mockito.when(startCommand.description()).thenReturn("register a user");
        Mockito.when(trackCommand.command()).thenReturn("/track");
        Mockito.when(trackCommand.description()).thenReturn("start tracking link");
        Mockito.when(untrackCommand.command()).thenReturn("/untrack");
        Mockito.when(untrackCommand.description()).thenReturn("stop tracking link");
        HelpCommand helpCommand = new HelpCommand(userService, List.of(listCommand, startCommand, trackCommand, untrackCommand));
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
        HelpCommand helpCommand = new HelpCommand(userService, List.of(listCommand, startCommand, trackCommand, untrackCommand));
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
