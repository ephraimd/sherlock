package ephraim.cron;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.google.gson.Gson;
import ephraim.App;
import ephraim.models.CronJob;
import ephraim.models.Task;
import ephraim.models.JsonSourcedTask;
import ephraim.models.TaskJsonModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskReader {
    private final ArrayList<Task> taskList = new ArrayList<>(60);

    public TaskReader(final String taskDataString, boolean isCronData) {
        if (isCronData) {
            parseCronStringData(Arrays.asList(taskDataString.split("\n")));
        } else {
            parseTaskFileData(taskDataString);
        }
    }

    private void parseTaskFileData(final String taskDataString) {
        CronParser unixCronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
        new Gson().fromJson(taskDataString, TaskJsonModel.class)
                .getJsonObjectList()
                .forEach(taskJsonObject -> {
                    JsonSourcedTask jsonSourcedTask = new JsonSourcedTask(taskJsonObject);
                    if(jsonSourcedTask.isEnabled()) {
                        jsonSourcedTask.setExecutionTime(ExecutionTime.forCron(unixCronParser.parse(taskJsonObject.getTimeString())));
                        taskList.add(jsonSourcedTask);
                    }
                });
    }

    private void parseCronStringData(final List<String> cronJobsDataList) {
        //parse the UNIX cron expression and get exec time
        CronParser unixCronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));

        for (String cronString : cronJobsDataList) {
            try {
                CronJob cronJobTmp = new CronJob(cronString, true);
                //Create ExecutionTime for a given cron expression.
                cronJobTmp.setExecutionTime(ExecutionTime.forCron(unixCronParser.parse(cronJobTmp.getTimeString())));
                taskList.add(cronJobTmp);
            } catch (IllegalArgumentException ex) {
                ex.addSuppressed(new IllegalStateException("Error occured while on Line Text: " + cronString));
                if (App.jLogger.isLogToFile()) {
                    App.jLogger.error(ex);
                }
                throw ex;
            } catch (Exception ex) {
                if (App.jLogger.isLogToFile()) {
                    App.jLogger.error(ex);
                }
                throw ex;
            }

        }

    }

    public List<Task> getTasks() {
        return taskList;
    }
}
