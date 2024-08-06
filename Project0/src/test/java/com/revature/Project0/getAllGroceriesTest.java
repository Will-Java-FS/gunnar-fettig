package com.revature.Project0;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.revature.services.LoggedInUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.revature.models.Grocery;
import com.fasterxml.jackson.databind.ObjectMapper;

public class getAllGroceriesTest {
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
    public void getAllGroceriesAvailable() throws IOException, InterruptedException
    {
        LoggedInUserService.getInstance().setLoggedInUser(null);
        String jsonlg = "{\"name\" : \"Bob\", \"password\" : \"Bob123\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);

        List<Grocery> expectedResult = new ArrayList<Grocery>();

        //expectedResult.add(new Grocery(1, "Apple", "Fruits", 1, new BigDecimal("0.99"), null));
        expectedResult.add(new Grocery(2, "Banana", "Fruits", 4, new BigDecimal("1.50"), null));

        List<Grocery> actualResult = objectMapper.readValue(response.body().toString(), new TypeReference<List<Grocery>>() {});
        Assertions.assertEquals(expectedResult, actualResult, "Expected="+expectedResult + ", Actual="+actualResult);

    }

    @Test
    public void getAllGroceriesAvailableAdmin() throws IOException, InterruptedException
    {
        LoggedInUserService.getInstance().setLoggedInUser(null);
        String jsonlg = "{\"name\" : \"Admin\", \"password\" : \"coolAdmin27\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries"))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);

        List<Grocery> expectedResult = new ArrayList<Grocery>();

        expectedResult.add(new Grocery(1, "Apple", "Fruits", 1, new BigDecimal("0.99"), null));
        expectedResult.add(new Grocery(2, "Banana", "Fruits", 4, new BigDecimal("1.50"), null));
        expectedResult.add(new Grocery(3, "mango", "Fruits", 5, new BigDecimal("2.22"), null));

        List<Grocery> actualResult = objectMapper.readValue(response.body().toString(), new TypeReference<List<Grocery>>() {});
        Assertions.assertEquals(expectedResult, actualResult, "Expected="+expectedResult + ", Actual="+actualResult);

    }
}
