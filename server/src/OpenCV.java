import ImageMatching.ImageTile;
import ImageMatching.TileDetector;
import org.opencv.core.*;

import java.util.List;

public class OpenCV {
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        TileDetector detector=new TileDetector(1600);

        detector.runOn("src/img/photos/img48.jpg","data2",false);


        List<List<ImageTile>> clusters=detector.findCluster();
        System.out.println(clusters.size());

        for (int i = 0; i < clusters.size(); i++) {

            for (int y=0;y<clusters.get(i).size();y++){
                System.out.println("Cluster " + (i + 1) + ": " + clusters.get(i).get(y).getName());
            }
        }


        System.out.println("fin");
    }






}
/*
Cluster 1: 0011
Cluster 1: 0026
Cluster 1: 0026
Cluster 2: 0006
Cluster 2: 0001
Cluster 3: 0033
Cluster 3: 0030
Cluster 3: 0030
Cluster 4: 0018
Cluster 4: 0015
Cluster 5: 0021
Cluster 5: 0007
Cluster 5: 0007
test

 */