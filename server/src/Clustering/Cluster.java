package Clustering;

import ImageService.ImageTile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.util.ArrayList;

public class Cluster extends ArrayList<ClusterPoint> {
    public void concat(Cluster cluster){
        for(var point : cluster){
            this.add(point);
        }
    }

    public JSONArray toJSONObject() {
        JSONArray obj = new JSONArray();

        for(var point : this){
            obj.put(point.toJSONObject());
        }

        return obj;
    }
}
