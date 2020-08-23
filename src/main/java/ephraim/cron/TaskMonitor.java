package ephraim.cron;

import ephraim.App;
import ephraim.Slot;
import ephraim.StaticJDispatcher;
import ephraim.comms.SignalConstants;
import ephraim.models.Message;
import ephraim.models.Task;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


/**
 * this guy stores the tasks with a watch limit tag(after which it pings ?)
 * once any event is received, it resolves that event against the list of tasks and notifies CronWatcher with a feedback
 */
public class TaskMonitor {
    /**
     * Every Task feedback is logged here from all CronTask Threads
     */
    public static List<TaskFeedback> feedbackList = new ArrayList<>(500);
    private static HashMap<String, Integer> runningTaskList = new HashMap<>(50);
    private final Task task;
    /**
     * THe combination of multiple events makes up a state, a state is updated on events,
     * a watcher watches the state under a timer. This is the State-Event Pattern.
     * - Needs a state updater, a watcher timer, and a watcher
     */
    //timer for state watching
    private int timeCount = 0;
    //state determinants to watch for (event) updates
    private Message lastMessage = null;
    //state updater
    private Slot slot;

    public TaskMonitor(final Task task){
        this.task = task;
    }

    public void monitor() {
        //let us know if an instance of the task is still running
        createTimer();
        slot = objects -> {
            Message message = (Message)objects[0];
            if(message.getMessageForm() != Message.Form.TASK_UPDATE){
                return;
            }
            String taskId = (String)message.getPayload();
            if(taskId.equals(task.getId())){
                //if the cronjob app sending a message matches the one this TaskMonitor instance is watching
                lastMessage = message;
            }
        };

        StaticJDispatcher.getDispatcher().addSlot(SignalConstants.APP_TASK_EVENT_LOG, slot); //state updater
    }

    private void establishTaskActive(){
        if(runningTaskList.containsKey(task.getId())){
            slackAlert(task.getId() + " has failed to start", "Test message body");
        }
    }
    private void createTimer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(task.isLongRunningApp() || timeCount < task.getEndTimeCheckLimit()){
                    if(timeCount > task.getStartTimeCheckLimit() && (null == lastMessage || lastMessage.getPayloadInference() == Message.PayloadInference.APP_NOT_STARTED)){
                        //alert, defer checking to later times for n number of times,
                        //after which you stop watching
                        slackAlert(task.getId() + " has failed to start", "Test message body");
                    }
                    if(!task.isLongRunningApp()){//slackAlert(task.getId() + " has failed to start", "Test message body");
                        if(timeCount > task.getEndTimeCheckLimit() &&
                                (null == lastMessage || lastMessage.getPayloadInference() == Message.PayloadInference.APP_TASK_STARTED)){
                            slackAlert(task.getId() + " is taking too long on runtime", "Test message body");
                        }
                    }else{
                        if(timeCount % task.getActivityWarnInterval() == 0 &&
                                (null == lastMessage || lastMessage.getPayloadInference() == Message.PayloadInference.APP_TASK_STARTED)){
                            slackAlert(task.getId() + " is still running", "Test message body"); //after x hours
                        }
                    }

                    ++timeCount; //only one thread is working on this, so no race condition expected
                }else{
                    timer.cancel();
                    runningTaskList.remove(task.getId());
                    StaticJDispatcher.getDispatcher().removeSlot(SignalConstants.APP_TASK_EVENT_LOG, slot);
                }
            }
        }, 0l, 1000l);
    }

    private void slackAlert(String title, String message){
        System.out.println(title + " <> "+ message);
        try {
            App.slackService.sendErrorMessage(title, message);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            if (App.jLogger.isLogToFile()) {
                App.jLogger.error(ex);
            }
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            if (App.jLogger.isLogToFile()) {
                App.jLogger.error(ex);
            }
        } catch (TimeoutException ex) {
            ex.printStackTrace();
            if (App.jLogger.isLogToFile()) {
                App.jLogger.error(ex);
            }
        }
    }

    public static List<TaskFeedback> getTaskActivityReport(){
        return feedbackList;
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
