package com.directsupply;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.URI;
import java.io.IOException;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello world!");
        JSONtoQuestionList(CreateURL(1, QuestionType.MULTIPLECHOICE), 10);

    }

    public static String CreateURL(int numQuestions, QuestionType type) {
        String outputString = "https://opentdb.com/api.php?amount=10&encode=url3986";
        return outputString;
    }

    public static Question[] JSONtoQuestionList(String JSONURL, int numberOfQuestions) throws IOException {
        String jsonStream = IOUtils.toString(new URL(JSONURL), StandardCharsets.ISO_8859_1);
        Question[] questionList = new Question[numberOfQuestions];

        System.out.println("Done Reading!");
        int endQuestionIndex = 0;
        //Avoid first curly brace
        int startQuestionIndex = 0;
        // Loop through each question
        for (int questionIndex = 0; questionIndex < numberOfQuestions; questionIndex++) {

            // Do Do string parser
            // Find First curly index and second cury brace index
            int currentIndex = endQuestionIndex+1;
            while (true) {
                if (jsonStream.charAt(currentIndex)=='{'){
                    startQuestionIndex = currentIndex;
                    currentIndex++;
                } else if (jsonStream.charAt(currentIndex) == '}'){
                    endQuestionIndex = currentIndex;
                    break;
                } else{
                    currentIndex++;
                }
            }
            String individualQuestionString = jsonStream.substring(startQuestionIndex, endQuestionIndex+1);

            // Find Second Curly index

            // Split string
            // Move to next string.

            Gson gson = new Gson();
            //Question question = gson.fromJson(individualQuestionString, Question.class);
            Question question = gson.fromJson(individualQuestionString, Question.class);
            System.out.println("Creating a MC question");
            questionList[questionIndex] = question;
        }

        return questionList;
    }
}