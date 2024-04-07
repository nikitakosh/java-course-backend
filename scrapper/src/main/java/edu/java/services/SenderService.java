package edu.java.services;

import edu.java.clients.client.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.controllers.dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SenderService {
    private final ScrapperQueueProducer queueProducer;
    private final BotClient botClient;
    private final ApplicationConfig config;

    public void send(LinkUpdate linkUpdate) {
        if (config.useQueue()) {
            queueProducer.send(linkUpdate);
        } else {
            botClient.sendMessage(linkUpdate);
        }
    }
}
