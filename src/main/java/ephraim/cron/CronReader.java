package ephraim.cron;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import ephraim.App;
import ephraim.models.CronJob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CronReader {
    private final ArrayList<CronJob> cronJobs = new ArrayList<>(60);

    public CronReader(final String cronJobsData) {
        parseCronStringData(Arrays.asList(cronJobsData.split("\n")));
    }

    private void parseCronStringData(final List<String> cronJobsDataList) {
        //parse the UNIX cron expression and get exec time
        CronParser unixCronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));

        for (String cronString : cronJobsDataList) {
            try {
                CronJob cronJobTmp = new CronJob(cronString, true);
                //Create ExecutionTime for a given cron expression.
                cronJobTmp.setExecutionTime(ExecutionTime.forCron(unixCronParser.parse(cronJobTmp.getCronTimeString())));
                cronJobs.add(cronJobTmp);
            } catch (IllegalArgumentException ex) {
                ex.addSuppressed(new IllegalStateException("Error occured while on Line Text: " + cronString));
                if (App.jLogger.isLogToFile()) {
                    App.jLogger.error(ex);
                }
                throw ex;
            }catch (Exception ex){
                if (App.jLogger.isLogToFile()) {
                    App.jLogger.error(ex);
                }
                throw ex;
            }

        }

    }

    public List<CronJob> getCronJobs() {
        return cronJobs;
    }

}
