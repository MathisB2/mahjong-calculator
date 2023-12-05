
import ImageService.*;
import org.opencv.core.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class OpenCV {
    public static void main(String[] args) {
        double time = System.currentTimeMillis();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        TileDetector detector = new TileDetector(new DataSet("data2"), 1600);
        TilesView view = new TilesView();

        var extractedTiles = detector.extractTiles("src/img/photos/img48.jpg");
        var matchedTiles = detector.getMatchedTilesTo(extractedTiles);

        System.out.println((System.currentTimeMillis() - time)/1000.0);
        view.showMatches(extractedTiles, matchedTiles);

       /* List<List<ImageTile>> clusters = detector.findCluster();

        for (int i = 0; i < clusters.size(); i++) {
            for (int y=0;y<clusters.get(i).size();y++){
                System.out.println("Cluster " + (i + 1) + ": " + clusters.get(i).get(y).getName());
            }
        }*/
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