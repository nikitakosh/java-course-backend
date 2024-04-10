package edu.java.bot.controllers;


import edu.java.bot.controllers.dto.LinkUpdate;
import edu.java.bot.exceptions.TooManyRequestsException;
import edu.java.bot.sender.MessageSender;
import edu.java.bot.utils.BucketGrabber;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BotController {
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private final MessageSender sender;
    private final BucketGrabber bucketGrabber;

    @PostMapping("/updates")
    public ResponseEntity<Void> sendMessage(
            @Valid @RequestBody LinkUpdate linkUpdate,
            HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            sender.send(
                    linkUpdate.getTgChatIds(),
                    linkUpdate.getDescription() + "\n"
                            + linkUpdate.getAdditionalInfo()
            );
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new TooManyRequestsException("too many requests");
    }

}
