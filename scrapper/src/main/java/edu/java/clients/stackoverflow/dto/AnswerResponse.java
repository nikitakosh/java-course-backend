package edu.java.clients.stackoverflow.dto;

import java.util.List;

public record AnswerResponse(
        List<AnswerItemResponse> items
) {
}
