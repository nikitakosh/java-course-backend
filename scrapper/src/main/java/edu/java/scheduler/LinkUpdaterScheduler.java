package edu.java.scheduler;

import edu.java.exceptions.IncorrrectURIException;
import edu.java.services.LinkService;
import edu.java.services.dto.LinkDTO;
import edu.java.services.updater.LinkUpdater;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final List<LinkUpdater> linkUpdaters;

    @Qualifier("jdbcLinkService")
    private final LinkService linkService;

    @Value("#{@scheduler.secondsThreshold()}")
    private long secondsThreshold;

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        List<LinkDTO> oldLinks = linkService.findOldLinks(secondsThreshold);
        log.info("in scheduler - " + secondsThreshold);
        for (LinkDTO link : oldLinks) {
            log.info(link.getUrl());
            LinkUpdater linkUpdater = linkUpdaters.stream()
                    .filter(updater -> updater.supports(URI.create(link.getUrl())))
                    .findFirst()
                    .orElseThrow(() -> new IncorrrectURIException("URI is incorrect"));
            linkUpdater.update(link);
        }
    }
}
