package edu.java.clients.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record ItemResponse(
        @JsonProperty("title") String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
) {

}
