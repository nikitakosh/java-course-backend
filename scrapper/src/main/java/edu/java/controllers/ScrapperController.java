package edu.java.controllers;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.LinkResponse;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ScrapperController {

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
            @PathVariable("id") Integer tgChatId
    ) {
        log.info("чат зарегистрирован");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
            @PathVariable("id") Integer tgChatId
    ) {
        log.info("чат успешно удалён");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId
    ) {
        log.info("ссылки успешно получены");
        return new ResponseEntity<>(
                new ListLinksResponse(),
                HttpStatus.OK
        );
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        log.info("ссылка успешна добавлена");
        return new ResponseEntity<>(
                new LinkResponse(tgChatId, addLinkRequest.getLink()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        log.info("ссылка успешна удалена");
        return new ResponseEntity<>(
                new LinkResponse(tgChatId, removeLinkRequest.getLink()),
                HttpStatus.OK
        );
    }
}
