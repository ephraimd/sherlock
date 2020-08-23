package ephraim.models;

public class Message {
    public static enum Form {TASK_UPDATE, COMMAND, UNKNOWN}
    public static enum PayloadInference {APP_TASK_STARTED, APP_TASK_ENDED, APP_NOT_STARTED}
    private Form messageForm = Form.UNKNOWN;
    private PayloadInference payloadInference = PayloadInference.APP_NOT_STARTED;
    private final Object payload;
    private final String remarks;

    public Message(final Object payload, String messageRemarks){
        this.payload = payload;
        this.remarks = messageRemarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public Form getMessageForm() {
        return messageForm;
    }

    public Object getPayload() {
        return payload;
    }

    public PayloadInference getPayloadInference() {
        return payloadInference;
    }
}
