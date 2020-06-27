package ephraim.comms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlackServiceTest {
    private final String slackWebHookUrl = "";

    @Test
    void sendErrorMessage() {
        SlackService slackService = new SlackService(slackWebHookUrl);
        Assertions.assertDoesNotThrow(() -> slackService.sendErrorMessage("Sherlock", "Running slack test for error messages"),
                "Slack error message service isn't functioning properly");
        Assertions.assertDoesNotThrow(() -> slackService.sendMessage("Sherlock", "Running slack test for ordinary messages"),
                "Slack service isn't functioning properly");
    }
}