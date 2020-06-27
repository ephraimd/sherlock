package ephraim.persistence;

import com.google.gson.Gson;
import ephraim.models.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigService {

    public static Config loadConfigFile(final String file) throws IOException {
        return new Gson().fromJson(Files.readString(Paths.get(file)), Config.class);
    }

}
