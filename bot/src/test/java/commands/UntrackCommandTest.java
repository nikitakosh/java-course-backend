package commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.models.Link;
import edu.java.bot.models.User;
import edu.java.bot.models.UserState;
import edu.java.bot.services.LinkService;
import edu.java.bot.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UntrackCommandTest {
    private static UserService userService;
    private static LinkService linkService;
    private static Update update;
    private static Message message;
    private static Chat chat;

    @BeforeAll
    public static void mockInit() {
        userService = Mockito.mock(UserService.class);
        linkService = Mockito.mock(LinkService.class);
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
    }

    @Test
    public void notRegisteredOrNeutralUser() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/untrack");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);
        Assertions.assertEquals(
                untrackCommand.handle(update).getParameters().get("text"),
                "Please register! Use command /start"
        );
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.of(new User()));
        Assertions.assertEquals(
                untrackCommand.handle(update).getParameters().get("text"),
                "Please, enter the link you want to unfollow"
        );
    }



    @Test
    public void userWithUntrackState() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("www.example.com");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        Link link = new Link();
        Mockito.when(linkService.findByUrl("www.example.com")).thenReturn(Optional.of(link));
        User user = new User();
        user.setState(UserState.UNTRACK);
        Mockito.when(userService.wasLinkTracked(user, link)).thenReturn(false);
        Mockito.when(userService.findByChatId(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);
        Assertions.assertEquals(
                untrackCommand.handle(update).getParameters().get("text"),
                "This link was not tracked"
        );

        user.setState(UserState.UNTRACK);
        user.setLinks(new ArrayList<>(List.of(link)));
        Mockito.when(userService.wasLinkTracked(user, link)).thenReturn(true);
        Assertions.assertEquals(
                untrackCommand.handle(update).getParameters().get("text"),
                "The link is no longer tracked"
        );
    }

    @Test
    public void commandAndDescription() {
        UntrackCommand untrackCommand = new UntrackCommand(userService, linkService);
        Assertions.assertEquals(
                untrackCommand.command(),
                "/untrack"
        );
        Assertions.assertEquals(
                untrackCommand.description(),
                "stop tracking link"
        );
    }
}
