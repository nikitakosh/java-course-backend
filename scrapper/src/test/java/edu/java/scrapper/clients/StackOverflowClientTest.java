package edu.java.scrapper.clients;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.stackoverflow.StackOverflowClientImpl;
import edu.java.clients.stackoverflow.dto.QuestionItemResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

@WireMockTest(httpPort = 8080)
public class StackOverflowClientTest {


    @Test
    public void fetchQuestion() {
        StackOverflowClientImpl stackOverflowClient = new StackOverflowClientImpl(WebClient.builder(), "http://localhost:8080");
        String id = "214741";
        stubFor(get(urlPathMatching(String.format("/questions/%s", id)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody("""
                                {   "items" :
                                        [
                                            {
                                                "title" : "What is a StackOverflowError?",
                                                "last_activity_date" : 1680213422
                                            }
                                        ]
                                }
                                """)));
        QuestionItemResponse itemResponse = stackOverflowClient.fetchQuestion(id);
        Assertions.assertEquals("What is a StackOverflowError?", itemResponse.title());
        Assertions.assertEquals(OffsetDateTime.parse("2023-03-30T21:57:02Z"), itemResponse.lastActivityDate());
    }
}
