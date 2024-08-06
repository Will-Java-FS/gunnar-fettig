package com.revature.Project0;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.revature.services.LoggedInUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class updateGroceryTest {
    ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objectMapper;


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
    public void updateGrocerySuccessful() throws IOException, InterruptedException
    {
        LoggedInUserService.getInstance().setLoggedInUser(null);
        String jsonlg = "{\"name\" : \"Bob\", \"password\" : \"Bob123\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());


        String json = "{\"name\" : \"mango\" , \"category\": \"Fruits\" , \"quantity\": 5, \"price\" : 2.2 }";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries/update/2"))
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        Integer actualResult = objectMapper.readValue(response.body().toString(), Integer.class);
        Assertions.assertTrue(actualResult.equals(1), "Expected to modify 1 row, but actually modified " + actualResult + " rows.");

    }

    @Test
    public void updateGroceryNotFound() throws IOException, InterruptedException
    {
        LoggedInUserService.getInstance().setLoggedInUser(null);
        String jsonlg = "{\"name\" : \"Bob\", \"password\" : \"Bob123\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());


        String json = "{\"quantity\": 10}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries/update/10"))
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(404, status, "Expected Status Code 200 - Actual Code was: " + status);

    }

    @Test
    public void updateGroceryInvalidPrice() throws IOException, InterruptedException
    {
        LoggedInUserService.getInstance().setLoggedInUser(null);
        String jsonlg = "{\"name\" : \"Bob\", \"password\" : \"Bob123\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());


        String json = "{\"price\": " + new BigDecimal(0) + "}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries/update/2"))
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(400, status, "Expected Status Code 200 - Actual Code was: " + status);
    }

    @Test
    public void updateGroceryInvalidQuantity() throws IOException, InterruptedException
    {
        LoggedInUserService.getInstance().setLoggedInUser(null);
        String jsonlg = "{\"name\" : \"Bob\", \"password\" : \"Bob123\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());


        String json = "{\"quantity\": -1}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries/update/2"))
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assertions.assertEquals(400, status, "Expected Status Code 200 - Actual Code was: " + status);
    }

}
