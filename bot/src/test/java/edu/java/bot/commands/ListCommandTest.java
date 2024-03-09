package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.Link;
import edu.java.bot.models.User;
import edu.java.bot.services.UserService;
import java.util.ArrayList;
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
public class ListCommandTest {
    @Mock
    private static UserService userService;
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Mock
    private static Chat chat;


    @Test
    public void notRegisteredUser() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        ListCommand listCommand = new ListCommand(userService);
        Assertions.assertEquals(
                listCommand.handle(update).getParameters().get("text"),
                "Please register! Use command /start"
        );
    }

    @Test
    public void userTrackedLinks() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        User user = new User();
        user.setLinks(new ArrayList<>());
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        ListCommand listCommand = new ListCommand(userService);
        Assertions.assertEquals(
                listCommand.handle(update).getParameters().get("text"),
                "There are no tracked links"
        );

        Link link1 = new Link();
        Link link2 = new Link();
        link1.setUrl("www.example1.com");
        link2.setUrl("www.example2.com");
        user.setLinks(new ArrayList<>(List.of(link1, link2)));
        Assertions.assertEquals(
                listCommand.handle(update).getParameters().get("text"),
                """
                        Tracked Links:\s
                        www.example1.com
                        www.example2.com"""
        );
    }

    @Test
    public void commandAndDescription() {
        ListCommand listCommand = new ListCommand(userService);
        Assertions.assertEquals(
                listCommand.command(),
                "/list"
        );
        Assertions.assertEquals(
                listCommand.description(),
                "show list of tracked links"
        );
    }
}
