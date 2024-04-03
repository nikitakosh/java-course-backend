package edu.java.bot.controllers;

import edu.java.bot.sender.MessageSender;
import edu.java.bot.utils.BucketGrabber;
import io.github.bucket4j.Bucket;
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
    @MockBean
    BucketGrabber bucketGrabber;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MessageSender sender;

    @SneakyThrows
    @Test
    public void sendMessageTest() {
        Mockito.doNothing().when(sender).send(Mockito.anyList(), Mockito.anyString());
        Bucket bucket = Mockito.mock(Bucket.class);
        Mockito.when(bucketGrabber.grabBucket(Mockito.anyString())).thenReturn(bucket);
        Mockito.when(bucket.tryConsume(Mockito.anyLong())).thenReturn(true);
        mvc.perform(post("/updates")
                        .header("X-Forwarded-For", "0.0.0.0")
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
