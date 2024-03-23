package edu.java.scrapper.controllers;


import edu.java.controllers.ScrapperController;
import edu.java.services.dto.LinkDTO;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaTgChatService;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScrapperController.class)
public class ScrapperControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private JpaLinkService linkService;
    @MockBean
    private JpaTgChatService chatService;


    @SneakyThrows
    @Test
    public void registerChatTest() {
        Mockito.doNothing().when(chatService).register(Mockito.anyLong());
        mvc.perform(post("/tg-chat/{id}", Mockito.anyLong()))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void unregisterChatTest() {
        Mockito.doNothing().when(chatService).unregister(Mockito.anyLong());
        mvc.perform(delete("/tg-chat/{id}", Mockito.anyLong()))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getLinksTest() {
        LinkDTO link1 = new LinkDTO();
        link1.setId(1);
        link1.setUrl("https://example1.com");
        LinkDTO link2 = new LinkDTO();
        link2.setId(2);
        link2.setUrl("https://example2.com");
        Mockito.when(linkService.listAll(Mockito.anyLong())).thenReturn(List.of(link1, link2));
        mvc.perform(get("/links").header("Tg-Chat-Id", Mockito.anyLong()))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertEquals(
                        result.getResponse().getContentAsString(),
                        "{\"links\":[{\"id\":1,\"url\":\"https://example1.com\"},{\"id\":2,\"url\":\"https://example2.com\"}],\"size\":2}"
                ));
    }

    @SneakyThrows
    @Test
    public void addLinkTest() {
        Mockito.doNothing().when(linkService).add(Mockito.anyLong(), Mockito.any());
        mvc.perform(post("/links")
                        .header("Tg-Chat-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "link": "https://example.com"
                                }"""))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void deleteLinkTest() {
        Mockito.doNothing().when(linkService).remove(Mockito.anyLong(), Mockito.any());
        mvc.perform(delete("/links")
                        .header("Tg-Chat-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "link": "https://example.com"
                                }"""))
                .andExpect(status().isOk());
    }
}
