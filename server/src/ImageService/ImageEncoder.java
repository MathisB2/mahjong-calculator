package ImageService;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class ImageEncoder {
    private final static String FORMAT = "jpg";
    private Encoder encoder;
    private Decoder decoder;

    ImageEncoder(){
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();
    }
    public Mat decode(String encodedImage){
        byte[] bytes = decoder.decode(encodedImage.split(",")[1]);
        Mat image = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_COLOR);

        return image;
    }

    public String encode(Mat image){
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode("."+FORMAT, image, matOfByte);

        return "data:image/" + FORMAT + ";base64," + encoder.encodeToString(matOfByte.toArray());
    }
}
