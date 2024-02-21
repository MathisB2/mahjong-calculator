package Clustering;

import org.json.JSONObject;

import java.awt.*;

/**
 * Abstract class to store coordinates of the extracted points from the input image.
 * The function toJSONObject must be overloaded
 */
public abstract class ClusterPoint extends Point {
    public abstract JSONObject toJSONObject();
}
