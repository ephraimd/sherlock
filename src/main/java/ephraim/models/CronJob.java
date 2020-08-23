package ephraim.models;

import com.cronutils.model.time.ExecutionTime;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CronJob implements Task{ //implement the Task Methods
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

    public boolean isUserCrontab() {
        return isUserCrontab;
    }

    public String getCronCommand() {
        return cronCommand;
    }

    public void setCronDescription(String cronDescription) {
        this.cronDescription = cronDescription;
    }

    public void setUserCrontab(boolean userCrontab) {
        isUserCrontab = userCrontab;
    }

    @Override
    public void setExecutionTime(ExecutionTime executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String getTimeString() {
        return cronTimeString;
    }

    @Override
    public String getId() {
        return fullCronString;
    }

    @Override
    public String getAppSecret() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isLongRunningApp() {
        return false;
    }

    @Override
    public long getStartTimeCheckLimit() {
        return 60;
    }

    @Override
    public long getEndTimeCheckLimit() {
        return 3600 * 7;
    }

    @Override
    public long getActivityWarnInterval() {
        return 3600;
    }

    @Override
    public long getExactLocalSendTime() {
        return -1;
    }
    @Override
    public LocalDateTime getNextExecutionTime() {
        return executionTime.nextExecution(ZonedDateTime.now()).orElseThrow().toLocalDateTime();
    }
    @Override
    public LocalDateTime getLastExecutionTime() {
        return executionTime.lastExecution(ZonedDateTime.now()).orElseThrow().toLocalDateTime();
    }

    @Override
    public long getTimeFromLastExecution() {
        return executionTime.timeFromLastExecution(ZonedDateTime.now()).orElseThrow().getSeconds();
    }

    @Override
    public long getTimeToNextExecution() {
        return executionTime.timeToNextExecution(ZonedDateTime.now()).orElseThrow().getSeconds();
    }
}
