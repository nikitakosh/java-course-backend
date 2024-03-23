package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;


    @Test
    public void handleTest() {
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
        HelpCommand helpCommand = new HelpCommand(List.of(
                listCommand,
                startCommand,
                trackCommand,
                untrackCommand
        ));
        Assertions.assertEquals(
                """
                        *Available commands:*
                        /list - show list of tracked links
                        /start - register a user
                        /track - start tracking link
                        /untrack - stop tracking link""",
                helpCommand.handle(update).getParameters().get("text")
        );
    }

}
