package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.controllers.dto.LinkResponse;
import edu.java.bot.controllers.dto.ListLinksResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    @Mock
    private ScrapperClient scrapperClient;


    @Test
    public void handleTest() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        ListLinksResponse listLinksResponse = new ListLinksResponse(
                List.of(new LinkResponse(1, "https://example1.com"), new LinkResponse(2, "https://example2.com")),
                2
        );
        Mockito.when(scrapperClient.getLinks(Mockito.anyLong())).thenReturn(listLinksResponse);
        ListCommand listCommand = new ListCommand(scrapperClient);
        Assertions.assertEquals(
                """
                        Tracked links:
                                                
                        https://example1.com
                        https://example2.com""",
                listCommand.handle(update).getParameters().get("text")
        );
    }
}
