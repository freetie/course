package com.github.freetie.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.config.location=classpath:test.properties")
public class SessionIntegrationTest {
    @LocalServerPort
    private int port;
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_USERNAME;
    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void resetDatabase() {
        Flyway flyway = Flyway.configure().dataSource(DB_URL, DB_USERNAME, DB_PASSWORD).load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void signupLoginLogout() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HashMap<String, String> testAccount = new HashMap<>();
        testAccount.put("username", "testUsername");
        testAccount.put("password", "testPassword");

        HttpRequest signupRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/account"))
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(testAccount)))
                        .build();
        HttpResponse<Void> signupResponse = httpClient.send(signupRequest, HttpResponse.BodyHandlers.discarding());
        Assertions.assertEquals(201, signupResponse.statusCode());

        HttpRequest loginRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/session"))
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(testAccount)))
                        .build();
        HttpResponse<String> loginResponse = httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        String cookie = loginResponse.headers().firstValue("Set-Cookie").orElse(null);
        Assertions.assertNotNull(cookie);

        HttpRequest sessionRequestAfterLogin =
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/session"))
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Cookie", cookie)
                        .GET()
                        .build();
        HttpResponse<String> sessionResponseAfterLogin = httpClient.send(sessionRequestAfterLogin, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(objectMapper.readValue(sessionResponseAfterLogin.body(), Map.class).get("username"), testAccount.get("username"));

        HttpRequest logoutRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/session"))
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Cookie", cookie)
                        .DELETE()
                        .build();
        Assertions.assertEquals(204, httpClient.send(logoutRequest, HttpResponse.BodyHandlers.ofString()).statusCode());

        HttpRequest sessionRequestAfterLogout =
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/session"))
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Cookie", cookie)
                        .GET()
                        .build();
        Assertions.assertEquals(401, httpClient.send(sessionRequestAfterLogout, HttpResponse.BodyHandlers.ofString()).statusCode());
    }
}
