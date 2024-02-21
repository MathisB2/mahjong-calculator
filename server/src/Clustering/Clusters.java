package Clustering;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class to manage Cluster objects. Usage is similar to ArrayList<Cluster>
 */
public class Clusters extends ArrayList<Cluster> {


    /**
     * Function to merge 2 clusters at the selected indexes
     * @param index1 the index of the first cluster
     * @param index2 the index of the second cluster
     */
    public void concat(int index1, int index2){
        Cluster cluster1 = this.get(index1);
        Cluster cluster2 = this.get(index2);

        cluster1.concat(cluster2);
        this.remove(index2);
    }

    /**
     * Function to convert the cluster list to JSON
     * @return the JSON object of the list
     */
    public JSONArray toJSONObject() {
        JSONArray obj = new JSONArray();

        for(var cluster : this){
            obj.put(cluster.toJSONObject());
        }

        return obj;
    }
}
