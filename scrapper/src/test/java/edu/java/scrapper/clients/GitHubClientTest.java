package edu.java.scrapper.clients;


import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import edu.java.github.GitHubClient;
import edu.java.github.GitHubClientImpl;
import edu.java.github.RepoResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class GitHubClientTest {


    private static WireMockServer wireMockServer;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public GitHubClientTest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @BeforeAll
    public static void beforeAll() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    public void fetchRepo() {
        GitHubClient gitHubClient = new GitHubClientImpl(webClientBuilder, "http://localhost:8080");
        String owner = "nikitakosh";
        String repo = "TinkJavaCourse";
        stubFor(get(urlPathMatching(String.format("/repos/%s/%s", owner, repo)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody("""
                                {
                                    "id": 703168142,
                                    "full_name": "nikitakosh/TinkJavaCourse",
                                    "updated_at": "2023-10-10T18:13:07Z"
                                }
                                """)));
        RepoResponse repoResponse = gitHubClient.fetchRepo(owner, repo);
        Assertions.assertEquals("703168142", repoResponse.id());
        Assertions.assertEquals("nikitakosh/TinkJavaCourse", repoResponse.fullName());
        Assertions.assertEquals(OffsetDateTime.parse("2023-10-10T18:13:07Z"), repoResponse.updatedAt());
    }
}
