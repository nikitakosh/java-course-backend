package edu.java.controllers;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ScrapperController {

    private final JdbcTgChatService chatService;
    private final JdbcLinkService linkService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
            @PathVariable("id") Integer tgChatId
    ) {
        chatService.register(tgChatId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
            @PathVariable("id") Integer tgChatId
    ) {
        chatService.unregister(tgChatId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId
    ) {
        ListLinksResponse listLinksResponse = linkService.listAll(tgChatId);
        return new ResponseEntity<>(listLinksResponse, HttpStatus.OK);
    }

    @PostMapping("/links")
    public ResponseEntity<Void> addLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        linkService.add(tgChatId, addLinkRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<Void> deleteLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        linkService.remove(tgChatId, removeLinkRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
