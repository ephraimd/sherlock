package ephraim.cron;

import ephraim.models.CronJob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Watches the crontasks, when they are due, passes them to TaskMonitor for evaluation whether and event has been received
 * for if they ran or not...feedback will be given
 */
public class CronWatcher {
    private final List<CronJob> cronJobsList;

    public CronWatcher(final List<CronJob> cronJobsList){
        this.cronJobsList = cronJobsList;
    }

    public void watchJobs(){
        ForkJoinPool jobsTaskPool = ForkJoinPool.commonPool();
        jobsTaskPool.submit(() -> {
            cronJobsList.parallelStream().forEach(cronJob -> {
                new CronTask(cronJob).act();
            });
        });
    }



    private class CronTask{
        private final CronJob cronJob;

        public CronTask(CronJob cronJob){
            this.cronJob = cronJob;
        }

        private TaskMonitor.TaskFeedback getFeedBack(){
            return TaskMonitor.monitor(cronJob);
        }

        public void act(){
            // getFeedBack as runnable, once done, re-call act!
            //schedule . reset
            //add to the list of feedback in synced manner since you're async
            ScheduledFuture<TaskMonitor.TaskFeedback> futureFeedback = Executors.newScheduledThreadPool(1)
                    .schedule(() -> getFeedBack(), cronJob.getTimeFromLastExecution(), TimeUnit.SECONDS);
            //futureFeedback.get();
        }
    }
}
