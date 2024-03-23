package edu.java.bot.controllers;

import edu.java.bot.sender.MessageSender;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BotController.class)
public class BotControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MessageSender sender;

    @SneakyThrows
    @Test
    public void sendMessageTest() {
        Mockito.doNothing().when(sender).send(Mockito.anyList(), Mockito.anyString());
        mvc.perform(post("/updates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "url": "https://example.com",
                                  "description": "string",
                                  "tgChatIds": [
                                    0
                                  ],
                                  "additionalInfo": "string",
                                  "anyAdditionalInfo": true
                                }"""))
                .andExpect(status().isOk());
    }
}
