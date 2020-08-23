package ephraim.models;

import com.cronutils.model.time.ExecutionTime;

import java.time.LocalDateTime;

public interface Task {
    public String getTimeString();
    public String getId();
    public String getAppSecret();
    public boolean isEnabled();
    public boolean isLongRunningApp();
    public long getStartTimeCheckLimit();
    public long getEndTimeCheckLimit();
    public long getActivityWarnInterval();
    public long getExactLocalSendTime();
    public LocalDateTime getNextExecutionTime();
    public LocalDateTime getLastExecutionTime();
    public long getTimeFromLastExecution();
    public long getTimeToNextExecution();
    public void setExecutionTime(ExecutionTime executionTime);
}
