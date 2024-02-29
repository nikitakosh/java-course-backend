package edu.java.bot.controllers;


import edu.java.bot.controllers.dto.LinkUpdate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BotController {

    @PostMapping("/updates")
    public ResponseEntity<Void> sendMessage(
            @Valid @RequestBody LinkUpdate linkUpdate
    ) {
        log.info("обновление обработано");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
