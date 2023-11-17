import ImageMatching.Tile;
import ImageMatching.TileDetector;
import org.opencv.core.*;

import java.util.ArrayList;
import java.util.List;

public class OpenCV {
    public static void main(String[] args) {
        System.out.println("test");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            TileDetector detector=new TileDetector(1600);


            detector.loadDataSet("data0");
            detector.loadImage("src/img/render10.png");
            detector.findContours();
            detector.showContours();
            detector.extractTiles();
            detector.matchAllTiles();

            List<List<Tile>> clusters=detector.findCluster();

            for (int i = 0; i < clusters.size(); i++) {

                for (int y=0;y<clusters.get(i).size();y++){
                    System.out.println("Cluster " + (i + 1) + ": " + clusters.get(i).get(y).getName()

                    );
                }
            }

        System.out.println("test");
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