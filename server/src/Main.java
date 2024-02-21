import ImageService.ImageService;
import NetworkService.NetworkService;
import ScoreService.ScoreService;
import org.opencv.core.Core;
public class Main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        ScoreService.load();
        ImageService.load();
    }
}