package edu.java.bot.controllers;


import edu.java.bot.controllers.dto.LinkUpdate;
import edu.java.bot.sender.MessageSender;
import jakarta.validation.Valid;
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
    private final MessageSender sender;

    @PostMapping("/updates")
    public ResponseEntity<Void> sendMessage(
            @Valid @RequestBody LinkUpdate linkUpdate
    ) {
        sender.send(
                linkUpdate.getTgChatIds(),
                linkUpdate.getDescription() + "\n"
                        + linkUpdate.getAdditionalInfo()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
