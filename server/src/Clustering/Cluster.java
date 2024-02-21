package Clustering;

import org.json.JSONArray;
import java.util.ArrayList;

/**
 * Class to manage ClusterPoint Objects. Usage is similar to ArrayList<ClusterPoint>
 */
public class Cluster extends ArrayList<ClusterPoint> {

    /**
     * Function to merge a cluster to the current one
     * @param cluster the cluster to merge
     */
    public void concat(Cluster cluster){
        for(var point : cluster){
            this.add(point);
        }
    }

    /**
     * Function to convert the cluster to JSON
     * @return the JSON object of the cluster
     */
    public JSONArray toJSONObject() {
        JSONArray obj = new JSONArray();

        for(var point : this){
            obj.put(point.toJSONObject());
        }

        return obj;
    }
}
