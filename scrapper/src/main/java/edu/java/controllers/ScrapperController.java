package edu.java.controllers;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.LinkResponse;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("scrapper-api/v1.0")
public class ScrapperController {

    @PostMapping("/tg-chat/{id}")
    public void registerChat(@PathVariable("id") Integer tgChatId) {
        log.info("чат зарегистрирован");
    }

    @DeleteMapping("/tg-chat/{id}")
    public void deleteChat(@PathVariable("id") Integer tgChatId) {
        log.info("чат успешно удалён");
    }

    @GetMapping("/links")
    public ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") Integer tgChatId) {
        log.info("ссылки успешно получены");
        return null;
    }

    @PostMapping("/links")
    public void addLink(@RequestHeader("Tg-Chat-Id") Integer tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        log.info("ссылка успешна добавлена");
    }

    @DeleteMapping("/links")
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") Integer tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        log.info("ссылка успешна удалена");
        return null;
    }


}
