package edu.java.clients.stackoverflow;

public interface StackOverflowClient {
    ItemResponse fetchQuestion(String id);

    boolean isSupport(String url);
}
