package edu.java.scheduler;

import edu.java.exceptions.IncorrrectURIException;
import edu.java.services.LinkService;
import edu.java.services.dto.LinkDTO;
import edu.java.services.updater.LinkUpdater;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class LinkUpdaterScheduler {

    private final List<LinkUpdater> linkUpdaters;


    private final LinkService linkService;

    @Value("#{@scheduler.secondsThreshold()}")
    private long secondsThreshold;

    @Autowired
    public LinkUpdaterScheduler(List<LinkUpdater> linkUpdaters, @Qualifier("jooqLinkService") LinkService linkService) {
        this.linkUpdaters = linkUpdaters;
        this.linkService = linkService;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        List<LinkDTO> oldLinks = linkService.findOldLinks(secondsThreshold);
        log.info("in scheduler - " + secondsThreshold);
        for (LinkDTO link : oldLinks) {
            log.info(link.getUrl() + " " + link.getCreatedAt());
            LinkUpdater linkUpdater = linkUpdaters.stream()
                    .filter(updater -> updater.supports(URI.create(link.getUrl())))
                    .findFirst()
                    .orElseThrow(() -> new IncorrrectURIException("URI is incorrect"));
            linkUpdater.update(link);
        }
    }
}
