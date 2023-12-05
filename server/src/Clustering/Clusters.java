package Clustering;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Clusters extends ArrayList<Cluster> {
    public void concat(int index1, int index2){
        Cluster cluster1 = this.get(index1);
        Cluster cluster2 = this.get(index2);

        cluster1.concat(cluster2);
        this.remove(index2);
    }

    public JSONArray toJSONObject() {
        JSONArray obj = new JSONArray();

        for(var cluster : this){
            obj.put(cluster.toJSONObject());
        }

        return obj;
    }
}
