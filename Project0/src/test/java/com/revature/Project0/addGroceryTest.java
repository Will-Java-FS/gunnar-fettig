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

import com.revature.models.Grocery;
import com.fasterxml.jackson.databind.ObjectMapper;


public class addGroceryTest {
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
    public void createMessageSuccessful() throws IOException, InterruptedException
    {
        String jsonlg = "{\"name\" : \"Goodman2\", \"password\" : \"doodman\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        String json = "{\"name\" : \"mango\" , \"category\": \"Fruits\" , \"quantity\": 5, \"price\" :" + new BigDecimal("2.22") + "}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        ObjectMapper om = new ObjectMapper();
        Grocery expectedResult = new Grocery(3, "mango", "Fruits" , 5, new BigDecimal("2.22") , null);
        Grocery actualResult = om.readValue(response.body().toString(), Grocery.class);
        Assertions.assertEquals(expectedResult, actualResult, "Expected="+expectedResult + ", Actual="+actualResult);
    }

    @Test
    public void createGroceryNotLogin() throws IOException, InterruptedException
    {
        LoggedInUserService.getInstance().setLoggedInUser(null);
        String json = "{\"name\" : \"mango\" , \"category\": \"Fruits\" , \"quantity\": 5, \"price\" :" + new BigDecimal("2.22") + "}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(401, status, "Expected Status Code 200 - Actual Code was: " + status);
    }

    @Test
    public void createGroceryNoName() throws IOException, InterruptedException
    {
        String jsonlg = "{\"name\" : \"Goodman2\", \"password\" : \"doodman\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        String json = "{\"name\" : null , \"category\": \"Fruits\" , \"quantity\": 5, \"price\" :" + new BigDecimal("2.22") + "}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(400, status, "Expected Status Code 200 - Actual Code was: " + status);
    }

    @Test
    public void createGroceryInvalidPrice() throws IOException, InterruptedException
    {
        String jsonlg = "{\"name\" : \"Goodman2\", \"password\" : \"doodman\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        String json = "{\"name\" : \"mango\" , \"category\": \"Fruits\" , \"quantity\": 5, \"price\" :" + new BigDecimal("-0.2") + "}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(400, status, "Expected Status Code 200 - Actual Code was: " + status);
    }

    @Test
    public void createGroceryInvalidQuantity() throws IOException, InterruptedException
    {
        String jsonlg = "{\"name\" : \"Goodman2\", \"password\" : \"doodman\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonlg))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> responselg = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        String json = "{\"name\" : \"mango\" , \"category\": \"Fruits\" , \"quantity\": -1, \"price\" :" + new BigDecimal("2.22") + "}";
        HttpRequest postGroceryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/groceries"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = webClient.send(postGroceryRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(400, status, "Expected Status Code 200 - Actual Code was: " + status);
    }


}
