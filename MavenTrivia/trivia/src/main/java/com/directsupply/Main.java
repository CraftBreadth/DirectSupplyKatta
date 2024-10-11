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
        String jsonAddress = IOUtils.toString(new URL(JSONURL), StandardCharsets.ISO_8859_1);
        JsonReader reader = new JsonReader(new StringReader(jsonAddress));
        Question[] questionList = new Question[numberOfQuestions];
        reader.beginObject();
        System.out.println("Ready to Read!");
        while (!reader.peek().equals(JsonToken.BEGIN_ARRAY)) {
            if (!reader.hasNext()) {
                System.out.println("String Parsed");
            }
            reader.skipValue();
        }
        System.out.println("Done Reading!");
        reader.beginArray();
        int questionIndex = 0;
        JsonToken token = reader.peek();
        while (true) {
            token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
            } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                reader.beginObject();
                // Check Question Type
                while (!reader.peek().equals(JsonToken.END_OBJECT)) {
                    // reader.skipValue();
                    token = reader.peek();
                    if (token.equals(JsonToken.NAME)) {
                        String tokenName = reader.nextName();
                        System.out.println(tokenName);
                        if (tokenName.equals("type")) {
                            break;
                        } else {
                            System.out.println("error");
                        }
                    } else {
                        System.out.println("error");
                    }
                }
                Question question = new Question();
                token = reader.peek();
                String typeName = reader.nextString();
                if (typeName.equals("multiple")) {
                    question = new MCQuestion(reader);
                    System.out.println("Creating a MC question");
                } else if (typeName.equals("boolean")) {
                    question = new TFQuestion(reader);
                    System.out.println("Creating a MC question");
                } else {
                    System.out.println("An error has occured!");
                }
                questionList[questionIndex] = question;
            } else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
            } else {
                // handleNonArrayToken(reader, token);
            }
            questionIndex++;
        }
        return questionList;
    }
}