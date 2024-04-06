import ImageService.ImageService;
import ScoreService.ScoreService;
import org.opencv.core.Core;
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
