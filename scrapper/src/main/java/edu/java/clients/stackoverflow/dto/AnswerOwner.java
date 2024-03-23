package edu.java.clients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnswerOwner(
        @JsonProperty("display_name")
        String displayName
) {
}
