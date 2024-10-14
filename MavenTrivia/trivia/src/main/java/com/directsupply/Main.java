package com.directsupply;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.io.Console;
import java.io.IOException;

import com.google.gson.Gson;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.io.IOUtils;

/* Quiz program. Basic program that runs a simple trivia quiz for the user. Questions are selected from the Open Trivia API and displayed to the user. The quiz is then scored.
 * 
 */

public class Main { // throws IOException, InterruptedException
    public static void main(String[] args) throws IOException {
        Console console = System.console();

        // Quiz Setup main loop. Users run quiz until they decide to exit.
        while (true) {
            boolean validated = false;
            int numQuestions = 0;
            while (!validated) {
                try {
                    numQuestions = Integer.parseInt(console.readLine("Enter the number of questions (Less than 50): "));
                } catch (Exception e) {
                    System.err.println("Invalid input. Only enter a number less than 50.");
                    continue;
                }
                if (numQuestions <= 50){
                    validated = true;
                } else {
                    System.err.println("Invalid input. Only enter a number less than 50.");
                }
            }
            String readQuestionType = "";
            // Validate the input until a proper input is entered
            validated = false;
            while (!validated) {
                try {
                    readQuestionType = console.readLine("Choose Multiple choice (MC) or True False (TF) or both (B): ");
                } catch (Exception IOException) {
                    System.err.println("Invalid input. Only enter MC, TF, or B");
                    continue;
                }
                if (!(readQuestionType.equals("MC") || readQuestionType.equals("TF") || readQuestionType.equals("B"))) {
                    System.err.println("Invalid input. Only enter MC, TF, or B");
                } else {
                    validated = true;
                }
            }

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
                // Randomly position the correct answer in the list of questions.
                Random r = new Random();
                int randomNumber = r.nextInt(currentQuestion.incorrect_answers.length);
                // Arrange incorrect and correct answers
                for (int answerNumber = 0; answerNumber < currentQuestion.incorrect_answers.length
                        + 1; answerNumber++) {
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
                int userAnswer = 0;
                validated = false;
                while (!validated) {
                    try {
                        userAnswer = Integer
                                .parseInt(console.readLine("Type the number associated with the correct answer: "));
                    } catch (Exception IOException) {
                        System.err.println("Invalid input. Only enter a number.");
                        continue;
                    }
                    if (!(userAnswer == 0 || userAnswer == 1 || userAnswer == 2 || userAnswer == 3
                            || userAnswer == 4)) {
                        System.err.println("Invalid input. Only enter a number.");
                    } else {
                        validated = true;
                    }
                }
                // Handle answers
                if (userAnswer != randomNumber) {
                    System.out.println("Incorrect! The correct answer was " + Integer.toString(randomNumber));
                } else {
                    System.out.println("Correct! Adding one to your score!");
                    score++;
                }
            }
            // Print final score
            System.out.println(
                    "Overall you got " + Integer.toString(score) + "/" + Integer.toString(numQuestions)
                            + " questions correct.");
            // Handle end of quiz
            validated = false;
            String readInput = "";
            while (!validated) {
                try {
                    readInput = console.readLine("Choose to (E)xit (E) or (T)ry again (T): ");
                } catch (Exception IOException) {
                    System.err.println("Invalid input. Only enter E or T");
                    continue;
                }
                if (!(readInput.equals("E") || readInput.equals("T"))) {
                    System.err.println("Invalid input. Only enter E, or T");
                } else {
                    validated = true;
                }
            }
            // Handle if the program should be closed
            if (readInput.equals("E")) {
                System.out.println("Closing program.");
                break;
            }

        }
    }
    /* Simple function that creates a url based on  inputs specifying number of questions and type of questions
     * returns string
     */
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

    /* Pulls data from url provided and then separates out the individual questions, decodes them from the JSON text and returns a list of Questions.
     * 
     */
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