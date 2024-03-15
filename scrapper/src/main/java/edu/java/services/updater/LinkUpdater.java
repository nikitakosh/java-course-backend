package edu.java.services.updater;

import edu.java.models.Link;
import java.net.URI;

public interface LinkUpdater {
    void update(Link link);

    boolean supports(URI link);
}
