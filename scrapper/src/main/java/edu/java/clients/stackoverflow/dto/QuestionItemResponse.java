package edu.java.clients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record QuestionItemResponse(
        @JsonProperty("title") String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
) {

}
