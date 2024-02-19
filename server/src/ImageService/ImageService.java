package ImageService;
import Clustering.ClusterDetector;
import Clustering.Clusters;
import NetworkService.*;

import org.java_websocket.WebSocket;
import org.opencv.core.Mat;

public class ImageService {
    private NetNamespace imageNet;
    private ClusterDetector clusterDetector;
    static private ImageService service = null;
    static public ImageService get(){
        if(service == null) load();
        return service;
    }

    static public void load(){
        assert service == null : "service is already loaded";
        service = new ImageService();
    }
    private ImageService() {
        imageNet = NetworkService.getNetwork().getNameSpace("ImageNet");
        clusterDetector = new ClusterDetector();

        DataSet dataSet2 = new DataSet("data2");
        dataSet2.save();

        ImageEncoder encoder = new ImageEncoder();
        imageNet.connect((WebSocket user, String encodedImage) -> {
            TileDetector detector = new TileDetector(dataSet2, 1600);
            Mat image = encoder.decode(encodedImage);

            var matchedTiles = detector.getMatchedTilesOn(image);

//            TilesView view = new TilesView();
//            view.showMatches(matchedTiles);

            Clusters clusters = clusterDetector.getClustersFrom(matchedTiles);
            return clusters.toJSONObject().toString();
        });
    }
}
