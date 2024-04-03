package edu.java.bot.sender;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ChangeTrackerBot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultMessageSender implements MessageSender {
    private final ChangeTrackerBot bot;

    @Override
    public void send(List<Long> tgChatIds, String message) {
        tgChatIds.forEach(chat -> bot.execute(new SendMessage(
                chat,
                message
        )));
    }
}
