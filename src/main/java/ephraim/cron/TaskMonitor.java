package ephraim.cron;

import ephraim.Slot;
import ephraim.StaticJDispatcher;
import ephraim.comms.SignalConstants;
import ephraim.models.CronJob;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * this guy stores the tasks with a watch limit tag(after which it pings ?)
 * once any event is received, it resolves that event against the list of tasks and notifies CronWatcher with a feedback
 */
public class TaskMonitor {
    /**
     * Every Task feedback is logged here from all CronTask Threads
     */
    private List<TaskFeedback> feedbackList = new ArrayList<>(500);
    private static final int START_WAIT_LIMIT = 60; //seconds
    private static final int END_WAIT_LIMIT = 3600 * 4; //seconds

    public List<TaskFeedback> getTaskActivityReport(){
        return feedbackList;
    }

    public static TaskFeedback monitor(final CronJob cronJob){
        AtomicInteger timeCount = new AtomicInteger(0);
        short jobStage = 0;  //0=not started, 1=started, 2=ended
        List<Object> watchingNotifObjects = new ArrayList<>(10);
        while(timeCount.get() < (cronJob.getTimeToNextExecution() + END_WAIT_LIMIT)){
            //
            timeCount.incrementAndGet();
        }

        Slot slot = new Slot() {
            @Override
            public void exec(Object... objects) {
                //start counting
                //if start, check time and add feedback,
                //if end, mark end type and feedback
                //if no start after count 1min, ping > feedback
                //if no end after start over 4hrs, ping > feedback
                //
                StaticJDispatcher.getDispatcher().removeSlot(SignalConstants.APP_EVENT_TASK_STARTED_LOG, this);
            }
        };

        StaticJDispatcher.getDispatcher().addSlot(SignalConstants.APP_EVENT_TASK_STARTED_LOG, slot);
        StaticJDispatcher.getDispatcher().addSlot(SignalConstants.APP_EVENT_TASK_STARTED_LOG, objects -> timeCount);
        return null;
    }

    public static class TaskFeedback{
        public static enum Form {FAILED_START, FAILED_STOP, FATAL_STOP, UNKNOWN}
        private final String notificationMessage;
        private final String notificationCronString;
        private Form form = Form.UNKNOWN;

        public TaskFeedback(final String notificationCronString, final String notificationMessage, Form feedbackForm){
            this.notificationCronString = notificationCronString;
            this.notificationMessage = notificationMessage;
            this.form = feedbackForm;
        }

        public String getNotificationMessage() {
            return notificationMessage;
        }

        public String getNotificationCronString() {
            return notificationCronString;
        }

        public Form getForm() {
            return form;
        }
    }
}
