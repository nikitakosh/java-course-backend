package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.AnswerItemResponse;
import edu.java.clients.stackoverflow.dto.QuestionItemResponse;

public interface StackOverflowClient {
    QuestionItemResponse fetchQuestion(String id);

    AnswerItemResponse fetchAnswer(String id);
}
