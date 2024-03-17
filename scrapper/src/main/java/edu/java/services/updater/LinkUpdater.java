package edu.java.services.updater;

import edu.java.services.dto.LinkDTO;
import java.net.URI;

public interface LinkUpdater {
    void update(LinkDTO link);

    boolean supports(URI link);
}
