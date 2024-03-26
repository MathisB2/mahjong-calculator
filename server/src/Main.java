import DataServices.DataService;
import DataServices.UsersService;
import ImageService.ImageService;
import NetworkService.NetworkService;
import ScoreService.ScoreService;
import org.opencv.core.Core;
import org.opencv.ml.ANN_MLP;

public class Main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        ScoreService.load();
        ImageService.load();

        // -- Data base connection modules (not working) -- //
        // DataService.load();
        // UsersService.load();
    }
}
