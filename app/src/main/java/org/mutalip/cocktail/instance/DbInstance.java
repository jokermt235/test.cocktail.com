package org.mutalip.cocktail.instance;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DbInstance {
    JSONArray getData();
    void save(JSONObject data);
    void syncLocal(JSONArray data);
}
