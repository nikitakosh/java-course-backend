package edu.java.clients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnswerItemResponse(
        @JsonProperty("answer_id")
        Long answerId,
        AnswerOwner owner
) {
}
