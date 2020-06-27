package ephraim.models;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CronJobTest {
    private static String cronTime = "10 7 26 6 5";

    @Test
    void getExecutionDetails() {
        CronJob job = new CronJob(cronTime, true);

        //parse the UNIX cron expression and get exec time
        CronParser unixCronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
        job.setExecutionTime(ExecutionTime.forCron(unixCronParser.parse(job.getCronTimeString())));

        Assertions.assertEquals(cronTime, job.getCronTimeString());
        Assertions.assertEquals("", job.getCronCommand());
        Assertions.assertTrue(job.getNextExecutionTime().isAfter(job.getLastExecutionTime()));
        Assertions.assertTrue(job.getNextExecutionTime().isAfter(ZonedDateTime.now().toLocalDateTime()));
    }

    @Test
    @Disabled
    public void roughtTest(){
        String quartzCronExpression = cronTime;
        CronParser quartzCronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));

        // parse the QUARTZ cron expression.
        Cron parsedQuartzCronExpression = quartzCronParser.parse(quartzCronExpression);

        // Create ExecutionTime for a given cron expression.
        ZonedDateTime now = ZonedDateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(parsedQuartzCronExpression);

        // Given a Cron instance, we can ask for next/previous execution
        System.out.println(String.format("Given the Quartz cron '%s' and reference date '%s', last execution was '%s'",
                parsedQuartzCronExpression.asString(), now, executionTime.lastExecution(now).get())
        );
        System.out.println(String.format("Given the Quartz cron '%s' and reference date '%s', next execution will be '%s'",
                parsedQuartzCronExpression.asString(), now, executionTime.nextExecution(now).get())
        );

        // or request time from last / to next execution
        System.out
                .println(String.format("Given the Quartz cron '%s' and reference date '%s', last execution was %s seconds ago",
                        parsedQuartzCronExpression.asString(), now, executionTime.timeFromLastExecution(now).get().getSeconds())
                );
        System.out.println(
                String.format("Given the Quartz cron '%s' and reference date '%s', next execution will be in %s seconds",
                        parsedQuartzCronExpression.asString(), now, executionTime.timeToNextExecution(now).get().getSeconds())
        );
    }

}