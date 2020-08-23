package ephraim.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MessageTest {
    private static final String jsonDump = "{\n" +
            "    \"eventType\": \"APP_STARTED\",\n" +
            "    \"fullCronString\": \"30 6 * * *  ephraim cat test4.txt && cp test4.txt newtest4.txt\",\n" +
            "    \"endedProperly\": true,\n" +
            "    \"isLongRunningApp\": true,\n" +
            "    \"remarks\": \"Yea, its working\",\n" +
            "    \"sentTimeStamp\": 987654\n" +
            "  }";

    @Test
    void allFieldAreCorrectlyPopulated() {
        Message message = new Message(jsonDump);
        Assertions.assertTrue(message.isEndedProperly());
        Assertions.assertTrue(message.isLongRunningApp());
        Assertions.assertEquals("Yea, its working", message.getRemarks());
        Assertions.assertEquals("30 6 * * *  ephraim cat test4.txt && cp test4.txt newtest4.txt", message.getFullCronString());
        Assertions.assertEquals(Message.Form.APP_STARTED, message.getMessageForm());
        Assertions.assertEquals(987654, message.getSentTimeStamp());
    }
}
