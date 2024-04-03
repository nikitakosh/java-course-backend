package edu.java.controllers;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.LinkResponse;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.exceptions.TooManyRequestsException;
import edu.java.services.LinkService;
import edu.java.services.TgChatService;
import edu.java.utils.BucketGrabber;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
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

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String TOO_MANY_REQUESTS = "too many requests";
    private final TgChatService chatService;
    private final LinkService linkService;
    private final BucketGrabber bucketGrabber;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
            @PathVariable("id") Integer tgChatId,
            HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            chatService.register(tgChatId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new TooManyRequestsException(TOO_MANY_REQUESTS);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
            @PathVariable("id") Integer tgChatId,
            HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            chatService.unregister(tgChatId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new TooManyRequestsException(TOO_MANY_REQUESTS);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getLinks(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            List<LinkResponse> listLinksResponse = linkService.listAll(tgChatId)
                    .stream()
                    .map(linkDTO -> new LinkResponse(linkDTO.getId(), linkDTO.getUrl()))
                    .toList();
            return new ResponseEntity<>(
                    new ListLinksResponse(
                        listLinksResponse,
                        listLinksResponse.size()),
                        HttpStatus.OK
            );
        }
        throw new TooManyRequestsException(TOO_MANY_REQUESTS);
    }

    @PostMapping("/links")
    public ResponseEntity<Void> addLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody AddLinkRequest addLinkRequest,
            HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            linkService.add(tgChatId, addLinkRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new TooManyRequestsException(TOO_MANY_REQUESTS);
    }

    @DeleteMapping("/links")
    public ResponseEntity<Void> deleteLink(
            @RequestHeader("Tg-Chat-Id") Integer tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest,
            HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            linkService.remove(tgChatId, removeLinkRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new TooManyRequestsException(TOO_MANY_REQUESTS);
    }
}
