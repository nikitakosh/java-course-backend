package edu.java.bot.controllers;


import edu.java.bot.controllers.dto.LinkUpdate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bot-api/v1.0")
@Slf4j
public class BotController {

    @PostMapping("/updates")
    public LinkUpdate sendMessage(@Valid @RequestBody LinkUpdate linkUpdate) {
        log.info("обновление обработано");
        return linkUpdate;
    }

}
