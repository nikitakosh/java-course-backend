package edu.java.stackoverflow;


import java.util.List;

public record QuestionResponse(
        List<ItemResponse> items
) {
}
