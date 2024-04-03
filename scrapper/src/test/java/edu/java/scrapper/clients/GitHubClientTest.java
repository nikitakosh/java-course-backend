package edu.java.scrapper.clients;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubClientImpl;
import edu.java.clients.github.RepoResponse;
import edu.java.configuration.clients.GitHubClientConfiguration;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

@WireMockTest(httpPort = 8080)
public class GitHubClientTest {

    @Test
    public void fetchRepo() {
        GitHubClientConfiguration clientConfiguration = new GitHubClientConfiguration(
                "http://localhost:8080",
                2,
                Duration.ofSeconds(2),
                "constant",
                List.of(404)
        );
        GitHubClient gitHubClient = new GitHubClientImpl(clientConfiguration, WebClient.builder());
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
