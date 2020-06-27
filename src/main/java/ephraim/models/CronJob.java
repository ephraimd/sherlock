package ephraim.models;

import com.cronutils.model.time.ExecutionTime;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CronJob {
    private String cronDescription = "";
    private boolean isUserCrontab = true;  //by default
    private String cronTimeString = "";
    private String cronCommand = "";
    private String fullCronString = "";
    private ExecutionTime executionTime;

    public CronJob(String fullCronString, boolean isUserCrontab){
        this.fullCronString = fullCronString;
        this.isUserCrontab = isUserCrontab;
        setCronTimeString();
        setCronCommand();
    }
    private void setCronTimeString(){
        this.cronTimeString = Arrays.stream(fullCronString.split(" ")).
                limit(5)
                .map(cronPartToken -> cronPartToken + " ")
                .collect(Collectors.joining())
                .trim();
    }

    public void setCronCommand() {
        this.cronCommand = Arrays.stream(fullCronString.split(" "))
                .skip(5) //skip first 5 so we can get the command || user + command
                .map(cronPartToken -> cronPartToken + " ")
                .collect(Collectors.joining())
                .trim();
    }

    public LocalDateTime getNextExecutionTime() {
        return executionTime.nextExecution(ZonedDateTime.now()).orElseThrow().toLocalDateTime();
    }
    public LocalDateTime getLastExecutionTime() {
        return executionTime.lastExecution(ZonedDateTime.now()).orElseThrow().toLocalDateTime();
    }

    public long getTimeFromLastExecution() {
        return executionTime.timeFromLastExecution(ZonedDateTime.now()).orElseThrow().getSeconds();
    }

    public long getTimeToNextExecution() {
        return executionTime.timeToNextExecution(ZonedDateTime.now()).orElseThrow().getSeconds();
    }

    public String getCronCommand() {
        return cronCommand;
    }
    public String getCronTimeString() {
        return cronTimeString;
    }

    public String getFullCronString() {
        return fullCronString;
    }
    public String getCronDescription() {
        return cronDescription;
    }

    public void setCronDescription(String cronDescription) {
        this.cronDescription = cronDescription;
    }

    public void setUserCrontab(boolean userCrontab) {
        isUserCrontab = userCrontab;
    }

    public void setExecutionTime(ExecutionTime executionTime) {
        this.executionTime = executionTime;
    }
    public boolean isUserCrontab() {
        return isUserCrontab;
    }
}
