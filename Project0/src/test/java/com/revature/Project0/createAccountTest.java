package com.revature.Project0;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class createAccountTest {
    ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objectMapper;

    /**
     * Before every test, reset the database, restart the Javalin app, and create a new webClient and ObjectMapper
     * for interacting locally on the web.
     * @throws InterruptedException
     */
    @BeforeEach
    public void setUp() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        String[] args = new String[] {};
        app = SpringApplication.run(Project0Application.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(500);
        SpringApplication.exit(app);
    }


    @Test
    public void createAccountSeccessful() throws IOException, InterruptedException
    {
        String json = "{\"name\" : \"Goodman2\", \"password\" : \"doodman\", \"is_admin\" : false}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected Status Code 200- Actual Code was: " + status);
    }

    @Test
    public void registerUserDuplicateUsername() throws IOException, InterruptedException {
        String json = "{\"name\" : \"Goodman3\", \"password\" : \"doodman\", \"is_admin\" : false}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response1 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status1 = response1.statusCode();
        int status2 = response2.statusCode();
        Assertions.assertEquals(200, status1, "Expected Status Code 200 - Actual Code was: " + status1);
        Assertions.assertEquals(409, status2, "Expected Status Code 409 - Actual Code was: " + status2);
    }

}
