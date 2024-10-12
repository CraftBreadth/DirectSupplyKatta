package com.directsupply;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class Question {
    public int rightAnswerNumber;
    public String difficulty;
    public String question;
    public String category;
    public String correct_answer;
    public String[] incorrect_answers;
    public String type;
}