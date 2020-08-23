package ephraim.models;

public class TaskJsonObject {
    private String timeString;
    private String id;
    private String appSecret;
    private boolean isEnabled = true;
    private boolean isLongRunningApp = true;
    private long startTimeCheckLimit;
    private long endTimeCheckLimit;
    private long exactLocalSendTime;
    private long activityWarnInterval;

    public long getEndTimeCheckLimit() {
        return endTimeCheckLimit;
    }

    public long getExactLocalSendTime() {
        return exactLocalSendTime;
    }

    public long getStartTimeCheckLimit() {
        return startTimeCheckLimit;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getId() {
        return id;
    }

    public String getTimeString() {
        return timeString;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isLongRunningApp() {
        return isLongRunningApp;
    }

    public long getActivityWarnInterval() {
        return activityWarnInterval;
    }
}