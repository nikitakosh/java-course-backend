package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;





}
