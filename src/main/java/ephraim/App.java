package ephraim;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import ephraim.comms.SlackService;
import ephraim.cron.TaskReader;
import ephraim.cron.TaskWatcher;
import ephraim.models.Config;
import ephraim.persistence.ConfigService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    @Parameter(names={"--crontab", "--ct"}, description = "Specify the command with which sherlock can get the cron jobs data. \nIf not set, uses tasklist file mode, --taskfile json file must be specified")
    private String crontabCommand = ""; //crontab -l

    @Parameter(names={"--logfile", "--lf"}, description = "Specify the logfile")
    private String logFile = "";

    @Parameter(names={"--taskfile", "--tf"}, description = "Specify the taskfile for when we're not using cron command")
    private String taskFile = "";

    //required = true,
    @Parameter(names={"--config", "--c"}, description = "Specify the config file containing sherlock's config data")
    private String configFile = "testconfig.json";

    public static Config config;
    public static JLogger jLogger = new JLogger();
    public static SlackService slackService;

    public static void main(String[] args) throws IOException, InterruptedException { //todo: slack's http request and send grid might work better in async
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
    private void setup() throws IOException, InterruptedException {
        if(!logFile.isEmpty()){
            jLogger.setLogFilePath(Paths.get(logFile));
        }
        setupConfig();
        slackService = new SlackService(config.getSlackWebHookUrl());
        setupTaskListReader();
    }

    private void setupConfig(){
        try {
            config = ConfigService.loadConfigFile(configFile);
        }catch(IOException ex){
            ex.addSuppressed(new IllegalStateException("Failed to initialize config data"));
            jLogger.error(ex);
        }
    }
    private void setupTaskListReader() throws IOException, InterruptedException {
        //add security check support
        TaskWatcher watcher;
        if(crontabCommand.isEmpty()){
            System.out.println("Detected normal Mode.");
            watcher = new TaskWatcher(new TaskReader(getTaskFileDataString(), false).getTasks());
        }else{
            System.out.println("Detected Cron Mode.");
            watcher = new TaskWatcher(new TaskReader(getCronDataString(), true).getTasks());
        }
        watcher.watchTasks();
    }

    private String getCronDataString() throws IOException, InterruptedException {
        Process process = new ProcessBuilder(crontabCommand.split(" ")).start();
        StringBuilder strBuf = new StringBuilder("");
        try(BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            String line;
            while ((line = buf.readLine()) != null){
                strBuf.append(line);
            }
        }
        process.waitFor();
        return strBuf.toString();
    }
    private String getTaskFileDataString() throws IOException {
        return Files.readString(Paths.get(taskFile));
    }
}
