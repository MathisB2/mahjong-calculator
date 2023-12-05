package Clustering;

import ImageService.ImageTile;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

public class ClusterDetector {
    public Clusters getClustersFrom(ArrayList<ImageTile> matchedTiles){
        Clusters clusters = new Clusters();
        for(int i = 0; i < matchedTiles.size(); i++){
            Cluster cluster = new Cluster();
            clusters.add(cluster);
            cluster.add(matchedTiles.get(i));
        }

        int i1, i2;
        double dist;

        while(clusters.size() > 5){
            dist = -1;
            i1 = 0;
            i2 = 0;
            for(int i = 0; i < clusters.size(); i++){
                for(int y = i + 1; y < clusters.size(); y++){
                    double d = dMin(clusters.get(i), clusters.get(y));
                    if(dist != -1 && dist >= dist) continue;

                    dist = d;
                    i1 = i;
                    i2 = y;
                }
            }

            clusters.concat(i1, i2);
        }

        return clusters;
    }

    private double dMin(Cluster l1, Cluster l2){
        double minDist = -1;

        for(int i = 0; i < l1.size(); ++i){
            for(int y = 0 ;y < l2.size(); ++y){
                double dist = getDistanceBetween(l1.get(i), l2.get(y));
                if(minDist != -1 && dist >= minDist) continue;

                minDist = dist;
            }
        }
        return minDist;
    }
    private double getDistanceBetween(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
