package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.Link;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.LinkService;
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
public class TrackCommandTest {
    @Mock
    private UserService userService;
    @Mock
    private LinkService linkService;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;


    @Test
    public void notRegisteredOrNeutralUser() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/track");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        TrackCommand trackCommand = new TrackCommand(userService, linkService);
        Assertions.assertEquals(
                trackCommand.handle(update).getParameters().get("text"),
                "Please register! Use command /start"
        );
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.of(new User()));
        Assertions.assertEquals(
                trackCommand.handle(update).getParameters().get("text"),
                "Please, enter the link you want to track"
        );
    }

    @Test
    public void userWithTrackState() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("www.example.com");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        Link link = new Link();
        link.setUrl("www.example.com");
        Mockito.when(linkService.findByUrl("www.example.com")).thenReturn(Optional.of(link));
        User user = new User();
        user.setState(UserState.TRACK);
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        TrackCommand trackCommand = new TrackCommand(userService, linkService);
        Assertions.assertEquals(
                trackCommand.handle(update).getParameters().get("text"),
                "Link is now tracked!"
        );
    }

    @Test
    public void commandAndDescription() {
        TrackCommand trackCommand = new TrackCommand(userService, linkService);
        Assertions.assertEquals(
                trackCommand.command(),
                "/track"
        );
        Assertions.assertEquals(
                trackCommand.description(),
                "start tracking link"
        );
    }
}
