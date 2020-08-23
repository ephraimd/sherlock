package ephraim.comms;

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
    private String payloadJsonTemplate = "{\n"
            + "    \"attachments\": [\n"
            + "        {\n"
            + "            \"fallback\": \"%s: %s\",\n"
            + "            \"color\": \"%s\",\n"
            + "\"pretext\": \"Apps Activity update from %s\","
            + "            \"author_name\": \"%s\",\n"
            + "            \"title\": \"%s\",\n"
            + "            \"text\": \"%s\",\n"
            + "        	\"footer\": \"Sherlock\"\n"
            + "        }\n"
            + "    ]\n"
            + "}";

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
        return String.format(payloadJsonTemplate,  appName, message, color, appName, appName, title, message);
    }

    public SlackService(String slackWebHookUrl){
        this.slackWebHookUrl = slackWebHookUrl;
    }

    public void sendErrorMessage(String title, String message) throws InterruptedException, ExecutionException, TimeoutException {
        send(buildMessagePayload(title, message, "#d1383d"));
    }
    public void sendMessage(String title, String message) throws InterruptedException, ExecutionException, TimeoutException {
        send(buildMessagePayload(title, message, "#2eb886"));
    }
}
