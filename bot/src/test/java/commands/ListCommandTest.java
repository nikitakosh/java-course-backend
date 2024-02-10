package commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.models.Link;
import edu.java.bot.models.User;
import edu.java.bot.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListCommandTest {

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
