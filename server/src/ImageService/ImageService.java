package ImageService;
import NetworkService.*;

import org.java_websocket.WebSocket;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import java.util.Base64;
import java.util.Base64.Decoder;

public class ImageService {
    NetNamespace imageNet;
    public ImageService(NetworkService networkService) {
        imageNet = networkService.newNameSpace("ImageNet");

        imageNet.connect((WebSocket user, String encodedImage) -> {
            double time = System.currentTimeMillis();
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            TileDetector detector = new TileDetector(new DataSet("data2"), 1600);
            TilesView view = new TilesView();

            var extractedTiles = detector.extractTiles(decodeImage(encodedImage));
            var matchedTiles = detector.getMatchedTilesTo(extractedTiles);

            System.out.println((System.currentTimeMillis() - time)/1000.0);
            view.showMatches(1600, extractedTiles, matchedTiles);

            return "yes";
        });
    }

    private Mat decodeImage(String encodedImage){
        Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(encodedImage.split(",")[1]);

        Mat image = new Mat();
        image.put(0,0, bytes);

        return image;
    }
}
