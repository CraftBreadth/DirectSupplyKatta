package com.directsupply;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class Question {
    private int rightAnswerNumber;
    private String difficulty;
    private String questionString;
    private String category;
    private String correctAnswer;

    /**
     * Handle non array non object tokens
     * 
     * @param reader
     * @param token
     * @throws IOException
     */
    public static void handleNonArrayToken(JsonReader reader, JsonToken token) throws IOException {
        if (token.equals(JsonToken.NAME))
            System.out.println(reader.nextName());
        else if (token.equals(JsonToken.STRING))
            System.out.println(reader.nextString());
        else if (token.equals(JsonToken.NUMBER))
            System.out.println(reader.nextDouble());
        else
            reader.skipValue();
    }

    /**
     * Handle a json array. The first token would be JsonToken.BEGIN_ARRAY.
     * Arrays may contain objects or primitives.
     * 
     * @param reader
     * @throws IOException
     */
    public static void handleArray(JsonReader reader) throws IOException {
        reader.beginArray();
        while (true) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.END_ARRAY)) {
                reader.endArray();
                break;
            } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
                handleObject(reader);
            } else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
            } else
                handleNonArrayToken(reader, token);
        }
    }

    /**
     * Handle an Object. Consume the first token which is BEGIN_OBJECT. Within
     * the Object there could be array or non array tokens. We write handler
     * methods for both. Noe the peek() method. It is used to find out the type
     * of the next token without actually consuming it.
     * 
     * @param reader
     * @throws IOException
     */
    private static void handleObject(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.BEGIN_ARRAY))
                handleArray(reader);
            else if (token.equals(JsonToken.END_OBJECT)) {
                reader.endObject();
                return;
            } else
                handleNonArrayToken(reader, token);
        }

    }

    public Question(JsonReader reader) throws IOException {
        // TODO Auto-generated constructor stub


    }

    public Question() {
        //TODO Auto-generated constructor stub
    }

    public int getRightAnswerNumber() {
        return this.rightAnswerNumber;

    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setRightAnswerNumber(int rightAnswerNumber) {
        this.rightAnswerNumber = rightAnswerNumber;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setValuesBasedOnJSON(JsonReader reader) {

    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public String getQuestionString() {
        return this.questionString;
    }

    public String getCategory() {
        return this.category;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

}

class TFQuestion extends Question {
    public TFQuestion(JsonReader reader) throws IOException {
        super(reader);
        //TODO Auto-generated constructor stub
    }

    private String incorrectAnswer;

}

class MCQuestion extends Question {
    public MCQuestion(JsonReader reader) {
        //TODO Auto-generated constructor stub
    }

    private String[] incorrectAnswers;
}
