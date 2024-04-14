package edu.java.clients.stackoverflow.dto;


import java.util.List;

public record QuestionResponse(
        List<QuestionItemResponse> items
) {
}
