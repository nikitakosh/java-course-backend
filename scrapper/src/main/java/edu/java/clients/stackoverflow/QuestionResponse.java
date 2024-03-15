package edu.java.clients.stackoverflow;


import java.util.List;

public record QuestionResponse(
        List<ItemResponse> items
) {
}
