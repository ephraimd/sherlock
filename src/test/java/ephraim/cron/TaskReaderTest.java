package ephraim.cron;

import ephraim.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

class TaskReaderTest {
    private static String testCronFile = "crontab";

    @BeforeAll
    public static void setup(){
        App.jLogger.fine("Testing CronReader using %s test cron data file", testCronFile);
        //App.jLogger.setLogFilePath(Paths.get("testlogging.txt"));
    }

    @Test
    public void isCorrectlyGettingListOfJobs() throws IOException {
        TaskReader taskReader = new TaskReader(Files.readString(Paths.get(testCronFile)));
        Assertions.assertEquals(4, taskReader.getTasks().size());
    }
    @Test
    public void getsCronStringCorrectly() throws IOException{
        TaskReader taskReader = new TaskReader(Files.readString(Paths.get(testCronFile)));
        //test the time string
        Assertions.assertLinesMatch(
                Arrays.asList("* * * * 1", "* 2 * * 4", "* 4 * * 6", "* 6 * * 0"),
                taskReader.getTasks().stream()
                        .map(cron -> cron.getCronTimeString())
                        .collect(Collectors.toList())
        );
        //test the command
        Assertions.assertLinesMatch(
                Arrays.asList("ephraim cat test.txt && cp test.txt newtest.txt",
                        "ephraim cat test2.txt && cp test2.txt newtest2.txt",
                        "ephraim cat test3.txt && cp test3.txt newtest3.txt",
                        "ephraim cat test4.txt && cp test4.txt newtest4.txt"),
                taskReader.getTasks().stream()
                        .map(cron -> cron.getCronCommand())
                        .collect(Collectors.toList())
        );
    }

    //todo: add tests for the timing

    /*System.out.println(
                String.format("Given the Quartz cron '%s' and reference date '%s', next execution will be in %s seconds",
                        parsedQuartzCronExpression.asString(), now, executionTime.timeToNextExecution(now).get().getSeconds())
        );*/
}
