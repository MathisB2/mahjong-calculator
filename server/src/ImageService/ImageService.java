package ImageService;
import Clustering.ClusterDetector;
import Clustering.Clusters;
import NetworkService.*;

import org.java_websocket.WebSocket;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Base64;
import java.util.Base64.Decoder;

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

        DataSet dataSet2 = new MultiDataSet(new String[]{"data2","data1"});
        ImageEncoder encoder = new ImageEncoder();
        imageNet.connect((WebSocket user, String encodedImage) -> {
            TileDetector detector = new TileDetector(dataSet2, 1600);
            Mat image = encoder.decode(encodedImage);
            System.out.print(encodedImage == encoder.encode(image));
            var extractedTiles = detector.extractTiles(image);
            var matchedTiles = detector.getMatchedTilesTo(extractedTiles);

            TilesView view = new TilesView();
            view.showMatches(extractedTiles, matchedTiles);

            Clusters clusters = clusterDetector.getClustersFrom(matchedTiles);
            return clusters.toJSONObject().toString();
        });
    }
}
