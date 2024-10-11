import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.http.HttpClient;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CreateURL(1, QuestionType.COMBINED))).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println("response body: " + response.body());
        System.out.println("Hello, World!");

    }

    public static String CreateURL(int numQuestions, QuestionType type) {
        String outputString = "https://opentdb.com/api.php?amount=10&type=multiple";
        return outputString;
    }
}
