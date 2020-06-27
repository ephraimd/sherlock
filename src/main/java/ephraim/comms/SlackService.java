package ephraim.comms;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SlackService {
    private final String slackWebHookUrl;
    private final String appName = "Sherlock";

    private void send(String payload) throws InterruptedException, ExecutionException, TimeoutException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.slackWebHookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(action -> System.out.println(action.statusCode()))
        .get(5, TimeUnit.MINUTES);
    }
    private String buildMessagePayload(String title, String message, String color){
        //todo: implement payload building
        return "";
    }

    public SlackService(String slackWebHookUrl){
        this.slackWebHookUrl = slackWebHookUrl;
    }

    public void sendErrorMessage(String title, String message) throws InterruptedException, ExecutionException, TimeoutException {
        send(buildMessagePayload(title, message, "red"));
    }
    public void sendMessage(String title, String message) throws InterruptedException, ExecutionException, TimeoutException {
        send(buildMessagePayload(title, message, "green"));
    }
}
