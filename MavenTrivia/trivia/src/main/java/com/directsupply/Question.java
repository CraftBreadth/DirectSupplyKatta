package com.directsupply;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class Question {
    private int rightAnswerNumber;
    private String difficulty;
    private String question;
    private String category;
    private String correct_answer;
    private String[] incorrect_answers;
    private String type;
}