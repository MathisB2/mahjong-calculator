package Clustering;

import ImageService.Tiles.MatchedTile;

import java.util.ArrayList;

/**
 * Class to detect and compute clusters from a list of MatchedTiles
 */
public class ClusterDetector {
    /**
     * Function to get a list of clusters from and ArrayList of MatchedTiles.
     * The function will return a maximum of 5 clusters.
     * @param points the ArrayList of MatchedTiles. Each MatchedTile must have valid and set coordinates
     * @return a Clusters object containing Cluster objects
     */
    public Clusters getClustersFrom(ArrayList<MatchedTile> points){
        Clusters clusters = new Clusters();
        for(int i = 0; i < points.size(); i++){
            Cluster cluster = new Cluster();
            clusters.add(cluster);
            cluster.add(points.get(i));
        }

        int i1, i2;
        double minDist;

        while(clusters.size() > 5){
            minDist = -1;
            i1 = 0;
            i2 = 0;
            for(int i = 0; i < clusters.size(); i++){
                for(int y = i + 1; y < clusters.size(); y++){
                    double dist = dMin(clusters.get(i), clusters.get(y));
                    if(minDist != -1 && dist >= minDist) continue;

                    minDist = dist;
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
    private double getDistanceBetween(ClusterPoint p1, ClusterPoint p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
