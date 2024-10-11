package com.directsupply;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpClient;
import java.net.URI;
import java.io.IOException;
import com.google.gson.Gson;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CreateURL(1, QuestionType.COMBINED))).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println("response body: " + response.body());
        System.out.println("Hello world!");
        String json = "...";

        Gson gson = new Gson();
    }

    public static String CreateURL(int numQuestions, QuestionType type) {
        String outputString = "https://opentdb.com/api.php?amount=10&type=multiple";
        return outputString;
    }
}