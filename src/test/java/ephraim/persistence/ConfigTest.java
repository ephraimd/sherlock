package ephraim.persistence;

import ephraim.models.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigTest {
    private static Config config;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> config = ConfigService.loadConfigFile("testconfig.json"),
                "Failed to initialize config class with file parameter");
    }

    @Test
    void getSlackWebHookUrl() {
        Assertions.assertEquals(config.getSlackWebHookUrl(), "slackUrl");
    }

    @Test
    public void getAwsSNSToken(){
        Assertions.assertEquals(config.getAwsSNSToken(), "awsToken");
    }

    @Test
    public void getCronJobStartDelayLimit(){
        Assertions.assertEquals(config.getCronJobStartDelayLimit(), 60);
    }

    @Test
    public void getCronJobStopDelayLimit(){
        Assertions.assertEquals(config.getCronJobStopDelayLimit(), 60);
    }
}