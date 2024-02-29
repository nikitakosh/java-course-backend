package edu.java.controllers;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.LinkResponse;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import java.util.List;
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

    private final ChatService chatService;
    private final LinkService linkService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
            @PathVariable("id") Integer tgChatId
    ) {
        chatService.registerChat(tgChatId);
        log.info("чат зарегистрирован");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
            @PathVariable("id") Integer tgChatId
    ) {
        chatService.removeChat(tgChatId);
        log.info("чат успешно удалён");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId
    ) {
        List<LinkResponse> linkResponses = linkService.getLinks(tgChatId).stream()
                .map(link -> new LinkResponse(1, link)).toList();
        log.info("ссылки успешно получены");
        return new ResponseEntity<>(
                new ListLinksResponse(linkResponses, linkResponses.size()),
                HttpStatus.OK
        );
    }

    @PostMapping("/links")
    public ResponseEntity<Void> addLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        linkService.addLink(tgChatId, addLinkRequest.getLink());
        log.info("ссылка успешна добавлена");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<Void> deleteLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        linkService.removeLink(tgChatId, removeLinkRequest.getLink());
        log.info("ссылка успешна удалена");
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
