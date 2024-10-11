
public class Question {
    int rightAnswerNumber;
    String questionString;
    int difficulty;
    String category;
    String correctAnswer;
}

class TFQuestion extends Question {
    String incorrectAnswer;
}

class MCQuestion extends Question {
    String[] incorrectAnswers;
}
