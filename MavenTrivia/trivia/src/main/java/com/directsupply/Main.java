package com.directsupply;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.io.Console;
import java.io.IOException;
import com.google.gson.Gson;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.io.IOUtils;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Console console = System.console();
        // Quiz Setup
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
        // Quiz taking
        int score = 0;
        Question currentQuestion;
        for (int questionNumber = 0; questionNumber < numQuestions; questionNumber++) {
            currentQuestion = questionList[questionNumber];
            // Print question number and question
            System.out.println("\nQuestion #" + Integer.toString(questionNumber + 1) + ".");
            System.out.println(URLDecoder.decode(currentQuestion.question, "UTF-8"));
            // Randomly postion the correct answer in the list of questions.
            Random r = new Random();
            int randomNumber = r.nextInt(currentQuestion.incorrect_answers.length);
            // Arrange incorrect and correct answers
            for (int answerNumber = 0; answerNumber < currentQuestion.incorrect_answers.length + 1; answerNumber++) {
                if (answerNumber == randomNumber) {
                    System.out.println(Integer.toString(answerNumber) + ".  "
                            + URLDecoder.decode(currentQuestion.correct_answer, "UTF-8"));
                } else {
                    // Need to realign the arrays if the answer has already been added
                    if (randomNumber < answerNumber) {
                        System.out.println(Integer.toString(answerNumber) + ".  "
                                + URLDecoder.decode(currentQuestion.incorrect_answers[answerNumber - 1], "UTF-8"));
                    } else {
                        System.out.println(Integer.toString(answerNumber) + ".  "
                                + URLDecoder.decode(currentQuestion.incorrect_answers[answerNumber], "UTF-8"));
                    }
                }
            }
            // Query user for correct answer
            int userAnswer = Integer.parseInt(console.readLine("Type the number associated with the correct answer:"));
            if (userAnswer != randomNumber) {
                System.out.println("Incorrect! The correct answer was " + Integer.toString(randomNumber));
            } else {
                System.out.println("Correct! Adding one to your score!");
                score++;
            }
        }
        System.out.println(
                "Overall you got " + Integer.toString(score) + "/" + Integer.toString(numQuestions) + " questions correct.");

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
            // Find start curly brace index and end curly brace index
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