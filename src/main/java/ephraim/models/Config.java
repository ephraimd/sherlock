package ephraim.models;

public class Config {
    private String awsSNSToken = "";
    private String slackWebHookUrl = "";
    private int cronJobStartDelayLimit = 50; //in seconds
    private int cronJobStopDelayLimit = 50; //in seconds

    public Config(){
        //
    }

    public String getSlackWebHookUrl() {
        return slackWebHookUrl;
    }

    public int getCronJobStartDelayLimit() {
        return cronJobStartDelayLimit;
    }

    public int getCronJobStopDelayLimit() {
        return cronJobStopDelayLimit;
    }

    public String getAwsSNSToken() {
        return awsSNSToken;
    }

    public void setAwsSNSToken(String awsSNSToken) {
        this.awsSNSToken = awsSNSToken;
    }

    public void setCronJobStartDelayLimit(int cronJobStartDelayLimit) {
        this.cronJobStartDelayLimit = cronJobStartDelayLimit;
    }

    public void setCronJobStopDelayLimit(int cronJobStopDelayLimit) {
        this.cronJobStopDelayLimit = cronJobStopDelayLimit;
    }

    public void setSlackWebHookUrl(String slackWebHookUrl) {
        this.slackWebHookUrl = slackWebHookUrl;
    }
}
