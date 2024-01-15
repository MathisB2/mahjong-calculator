package ImageService;
import Clustering.Cluster;
import Clustering.ClusterDetector;
import Clustering.Clusters;
import NetworkService.*;

import org.java_websocket.WebSocket;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.Base64.Decoder;

public class ImageService {
    NetNamespace imageNet;
    ClusterDetector clusterDetector;
    public ImageService(NetworkService networkService) {
        imageNet = networkService.newNameSpace("ImageNet");
        clusterDetector = new ClusterDetector();

        DataSet dataSet2 = new DataSet("data2");

        imageNet.connect((WebSocket user, String encodedImage) -> {
            System.out.println("new image from user");
            TileDetector detector = new TileDetector(dataSet2, 1600);
            TilesView view = new TilesView();

            var extractedTiles = detector.extractTiles(decodeImage(encodedImage));
            var matchedTiles = detector.getMatchedTilesTo(extractedTiles);

            view.showMatches(extractedTiles, matchedTiles);
            Clusters clusters = clusterDetector.getClustersFrom(matchedTiles);

            return clusters.toJSONObject().toString();
        });
    }

    private Mat decodeImage(String encodedImage){
        Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(encodedImage.split(",")[1]);

        Mat image = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_COLOR);

        return image;
    }
}
