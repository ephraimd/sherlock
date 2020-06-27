package ephraim.util;

import ephraim.JLogger;

import java.nio.file.Paths;
import java.util.Optional;

/**
 * No longer needed since the JLogger can work on its own
 */
@Deprecated
public class Logging {

    private static JLogger jLogger = new JLogger();

    public static void setupJLogger(Optional<String> logFile){
        logFile.ifPresent(file -> jLogger.setLogFilePath(Paths.get(file)));
    }
    public static JLogger getjLogger(){
        return jLogger;
    }
}
