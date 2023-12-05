import ImageService.ImageService;
import NetworkService.NetworkService;
import ScoreService.ScoreService;
import org.opencv.core.Core;

public class Main {
    public static void main(String[] args){
        NetworkService networkService = NetworkService.getNetworkOnPort(8080);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ImageService imageService = new ImageService(networkService);
        ScoreService scoreService = new ScoreService(networkService);
    }
}
