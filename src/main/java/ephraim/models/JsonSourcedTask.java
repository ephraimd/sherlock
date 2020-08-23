package ephraim.models;

import com.cronutils.model.time.ExecutionTime;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Tasks can be json sourced, yaml or cron sourced
 */
public class JsonSourcedTask implements Task{
    private ExecutionTime executionTime;
    private final TaskJsonObject taskObject;

    public JsonSourcedTask(final TaskJsonObject taskObject){
        this.taskObject =  taskObject;
    }

    @Override
    public String getTimeString() {
        return taskObject.getTimeString();
    }

    @Override
    public String getId() {
        return taskObject.getId();
    }

    @Override
    public String getAppSecret() {
        return taskObject.getAppSecret();
    }

    @Override
    public boolean isEnabled() {
        return taskObject.isEnabled();
    }

    @Override
    public boolean isLongRunningApp() {
        return taskObject.isLongRunningApp();
    }

    @Override
    public long getStartTimeCheckLimit() {
        return taskObject.getStartTimeCheckLimit();
    }

    @Override
    public long getEndTimeCheckLimit() {
        return taskObject.getEndTimeCheckLimit();
    }

    @Override
    public long getActivityWarnInterval() {
        return taskObject.getActivityWarnInterval();
    }

    @Override
    public long getExactLocalSendTime() {
        return taskObject.getExactLocalSendTime();
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

    @Override
    public void setExecutionTime(ExecutionTime executionTime) {
        this.executionTime = executionTime;
    }
}
