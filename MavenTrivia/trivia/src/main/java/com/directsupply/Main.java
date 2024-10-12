package com.directsupply;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.URI;
import java.io.Console;
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
        Console console = System.console();
        int numQuestions = Integer.parseInt(console.readLine("Enter the number of questions: "));
        String readQuestionType = console.readLine("Choose Multiple choice (MC) or True False (TF) or both (B)");
        QuestionType questionType = QuestionType.COMBINED;
        if (readQuestionType.equals("TF")) {
            questionType = QuestionType.TRUEFALSE;
        } else if (readQuestionType.equals("MC")) {
            questionType = QuestionType.MULTIPLECHOICE;
        } else if (readQuestionType.equals("B")) {
            questionType = QuestionType.COMBINED;
        } else {
            System.out.println("Invalid selection, using combined.");
        }
        Question[] questionList = JSONtoQuestionList(CreateURL(numQuestions, questionType), numQuestions);

    }

    public static String CreateURL(int numQuestions, QuestionType type) {
        String questionTypeSelect = "";
        if (type.equals(QuestionType.MULTIPLECHOICE)) {
            questionTypeSelect = "&type=multiple";
        } else if (type.equals(QuestionType.TRUEFALSE)) {
            questionTypeSelect = "&type=boolean";
        }

        String outputString = "https://opentdb.com/api.php?amount=" + Integer.toString(numQuestions)
                + questionTypeSelect + "&encode=url3986";
        return outputString;
    }

    public static Question[] JSONtoQuestionList(String JSONURL, int numberOfQuestions) throws IOException {
        System.out.println("Preparing Questions!");
        String jsonStream = IOUtils.toString(new URL(JSONURL), StandardCharsets.ISO_8859_1);
        Question[] questionList = new Question[numberOfQuestions];

        int endQuestionIndex = 0;
        // Avoid first curly brace
        int startQuestionIndex = 0;
        // Loop through each question
        for (int questionIndex = 0; questionIndex < numberOfQuestions; questionIndex++) {
            // Find start curly index and end brace index
            int currentIndex = endQuestionIndex + 1;
            while (true) {
                if (jsonStream.charAt(currentIndex) == '{') {
                    startQuestionIndex = currentIndex;
                    currentIndex++;
                } else if (jsonStream.charAt(currentIndex) == '}') {
                    endQuestionIndex = currentIndex;
                    break;
                } else {
                    currentIndex++;
                }
            }
            // Split string via string indexes
            String individualQuestionString = jsonStream.substring(startQuestionIndex, endQuestionIndex + 1);
            Gson gson = new Gson();
            // Create question based off of the string provided
            Question question = gson.fromJson(individualQuestionString, Question.class);
            questionList[questionIndex] = question;
        }
        System.out.println("Questions Ready!");
        return questionList;
    }
}