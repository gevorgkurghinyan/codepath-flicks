package com.gevkurg.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkurghin on 9/15/17.
 */

public class YoutubeTrailer {

    private String name;
    private String size;
    private String source;
    private String type;

    public YoutubeTrailer(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.size = jsonObject.getString("size");
        this.source = jsonObject.getString("source");
        this.type = jsonObject.getString("type");
    }

    public static List<YoutubeTrailer> fromJSONArray(JSONArray array) {
        List<YoutubeTrailer> result = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            try {
                result.add(new YoutubeTrailer(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }
}
