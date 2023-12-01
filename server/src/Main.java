import ImageService.ImageService;
import NetworkService.NetworkService;
import ScoreService.ScoreService;

public class Main {
    public static void main(String[] args){
        NetworkService networkService = NetworkService.getNetworkOnPort(8080);
        ImageService imageService = new ImageService(networkService);
        ScoreService scoreService = new ScoreService(networkService);
    }
}
