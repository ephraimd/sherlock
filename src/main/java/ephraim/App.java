package ephraim;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import ephraim.cron.CronReader;
import ephraim.cron.CronWatcher;
import ephraim.models.Config;
import ephraim.persistence.ConfigService;

import java.io.*;
import java.nio.file.Paths;

public class App {
    @Parameter(names={"--crontab", "--ct"}, description = "Specify the command with which sherlock can get the cron jobs data")
    private String crontabCommand = "crontab -l";

    @Parameter(names={"--logfile", "--lf"}, description = "Specify the logfile")
    private String logFile = "";

    //required = true,
    @Parameter(names={"--config", "--c"}, description = "Specify the config file containing sherlock's config data")
    private String configFile = "testconfig.json";

    public static Config config;
    public static JLogger jLogger = new JLogger();

    public static void main(String[] args) throws IOException{ //todo: slack's http request and send grid might work better in async
        App app = new App();
        JCommander.newBuilder()
                .addObject(app)
                .build()
                .parse(args);
        app.setup();
    }

    public App(){
        //
    }
    private void setup() throws IOException{
        setupConfig();
        setupCronReader();
        if(!logFile.isEmpty()){
            jLogger.setLogFilePath(Paths.get(logFile));
        }
    }

    private void setupConfig(){
        try {
            config = ConfigService.loadConfigFile(configFile);
        }catch(IOException ex){
            ex.addSuppressed(new IllegalStateException("Failed to initialize config data"));
            jLogger.error(ex);
        }
    }
    private void setupCronReader() throws IOException {
        //add security check support
        Process process = new ProcessBuilder(crontabCommand.split(" ")).start();
        StringBuilder commandOutput = new StringBuilder("");
        try(BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            String line;
            while ((line = buf.readLine()) != null){
                commandOutput.append(line);
            }
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CronWatcher watcher = new CronWatcher(new CronReader(commandOutput.toString()).getCronJobs());
        watcher.watchJobs();
    }
}
