package ephraim.models;

import java.util.ArrayList;
import java.util.List;

public class TaskJsonModel {
    private List<TaskJsonObject> jsonObjectList = new ArrayList<>(60);

    public List<TaskJsonObject> getJsonObjectList() {
        return jsonObjectList;
    }
}
