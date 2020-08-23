package ephraim.cron;

import ephraim.App;
import ephraim.models.Task;

import java.util.List;
import java.util.concurrent.*;

/**
 * Watches the crontasks, when they are due, passes them to TaskMonitor for evaluation whether and event has been received
 * for if they ran or not...feedback will be given
 */
public class TaskWatcher {
    private final List<Task> taskList;

    public TaskWatcher(final List<Task> taskList){
        this.taskList = taskList;
    }

    public void watchTasks(){
        taskList.parallelStream().forEach(task -> {
            try {
                new MonitorActor(task).act();
            } catch (ExecutionException ex) {
                if (App.jLogger.isLogToFile()) {
                    App.jLogger.error(ex);
                }
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                if (App.jLogger.isLogToFile()) {
                    App.jLogger.error(ex);
                }
                ex.printStackTrace();
            }
        });
    }


    private class MonitorActor {
        private final Task task;

        public MonitorActor(Task task){
            this.task = task;
        }

        public void act() throws ExecutionException, InterruptedException {
            // getFeedBack as runnable, once done, re-call act!
            //schedule . reset
            //add to the list of feedback in synced manner since you're async
            Executors.newScheduledThreadPool(1)
                    .schedule(
                            () -> new TaskMonitor(task).monitor(),
                            task.getTimeToNextExecution(), TimeUnit.SECONDS
                    )
                    .get();
        }
    }
}
