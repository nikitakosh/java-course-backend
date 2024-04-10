package edu.java.bot.services;

import edu.java.bot.controllers.dto.LinkUpdate;
import edu.java.bot.sender.DefaultMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaListenerService {

    private final DefaultMessageSender messageSender;

    @KafkaListener(topics = "${app.scrapper-topic-name}", containerFactory = "linkUpdateContainerFactory")
    public void listen(@Payload LinkUpdate update, Acknowledgment acknowledgment) {
        log.info("Link update info received: " + update);
        messageSender.send(
                update.getTgChatIds(),
                update.getDescription() + "\n"
                        + update.getAdditionalInfo()
        );
        acknowledgment.acknowledge();
    }
}
