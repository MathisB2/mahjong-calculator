package Clustering;

import org.json.JSONArray;

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
