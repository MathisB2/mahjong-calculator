package Clustering;

import ImageService.Tiles.MatchedTile;
import NetworkService.Tuple;

import java.util.ArrayList;

public class ClusterDetector {
    public Clusters getClustersFrom(ArrayList<MatchedTile> points){
        Clusters clusters = new Clusters();
        for(int i = 0; i < points.size(); i++){
            Cluster cluster = new Cluster();
            clusters.add(cluster);
            cluster.add(points.get(i));
        }

        int i1, i2;
        double minDist;
        int nbPoints = points.size();
        if(nbPoints <= 1) return clusters;

        double[] distances = new double[nbPoints - 1];
        Clusters[] data = new Clusters[nbPoints -1];

        for(int k = 0; k < nbPoints -1; ++k){
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

            distances[k] = minDist;
            clusters.concat(i1, i2);
            data[k] = (Clusters) clusters.clone();
        }


        var jumpInfo = getMaxJump(distances);

        if (jumpInfo.obj1 == -1){
            return clusters;
        }

        return data[jumpInfo.obj1];
    }

    private Tuple<Integer, Double> getMaxJump(double[] distances){
        double maxJump = -1;
        int index = -1;

        for(int i = 0; i < distances.length - 1; ++i){
            double jump = distances[i + 1] - distances[i];

            if(maxJump < jump) {
                maxJump = jump;
                index = i;
            }
        }

        return new Tuple(index, maxJump);
    }

    private double dMin(Cluster l1, Cluster l2){
        double minDist = -1;

        for(int i = 0; i < l1.size(); ++i){
            for(int y = 0; y < l2.size(); ++y){
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
