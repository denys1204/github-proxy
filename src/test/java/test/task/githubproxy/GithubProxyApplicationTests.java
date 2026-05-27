package test.task.githubproxy;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubProxyApplicationTests {
	private static final WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

	@LocalServerPort
	private int port;

	private RestClient restClient;

	@BeforeAll
	static void setUpAll() {
		wireMockServer.start();
		WireMock.configureFor(wireMockServer.port());
	}

	@AfterAll
	static void tearDownAll() {
		wireMockServer.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("github.api.url", wireMockServer::baseUrl);
	}

	@BeforeEach
	void setUpClient() {
		this.restClient = RestClient.create("http://localhost:" + port);
	}

	@Test
	void shouldReturnUserRepositories() {
		var username = "testuser";

		stubFor(get(urlEqualTo("/users/" + username + "/repos"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBody("""
                                [
                                  {"name": "repo1", "fork": false, "owner": {"login": "testuser"}},
                                  {"name": "repo2", "fork": true, "owner": {"login": "testuser"}}
                                ]
                                """)));

		stubFor(get(urlEqualTo("/repos/" + username + "/repo1/branches"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBody("""
                                [
                                  {"name": "main", "commit": {"sha": "12345abcde"}}
                                ]
                                """)));

		var responseEntity = restClient.get()
				.uri("/api/v1/github/" + username)
				.retrieve()
				.toEntity(Repository[].class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());

		Repository[] repositories = responseEntity.getBody();
		assertEquals(1, repositories.length);
		assertEquals("repo1", repositories[0].repositoryName());
		assertEquals("main", repositories[0].branches().getFirst().name());
		assertEquals("12345abcde", repositories[0].branches().getFirst().lastCommitSha());
	}

	@Test
	void shouldReturn404WhenUserNotFound() {
		var username = "nonexistinguser";

		stubFor(get(urlEqualTo("/users/" + username + "/repos"))
				.willReturn(aResponse()
						.withStatus(404)));

		try {
			restClient.get()
					.uri("/api/v1/github/" + username)
					.retrieve()
					.toBodilessEntity();
			fail("Expected HttpClientErrorException");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}
}
