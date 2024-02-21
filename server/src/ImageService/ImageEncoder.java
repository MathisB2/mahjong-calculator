package ImageService;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * Class containing tools to encode/decode base64 images
 */
public class ImageEncoder {
    private final static String FORMAT = "jpg";
    private Encoder encoder;
    private Decoder decoder;

    /**
     * Constructor of ImageEncoder
     */
    public ImageEncoder(){
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();
    }

    /**
     * Function to decode an image encoded in base64.
     * @param encodedImage String containing an image encoded in base64
     * @return Mat image that corresponds to the input String
     */
    public Mat decode(String encodedImage){
        byte[] bytes = decoder.decode(encodedImage.split(",")[1]);
        Mat image = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_COLOR);

        return image;
    }

    /**
     * Function to encode a Mat image to base64 String
     * @param image the Mat image to encode
     * @return a String containing the image data encoded in base64
     */
    public String encode(Mat image){
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode("."+FORMAT, image, matOfByte);

        return "data:image/" + FORMAT + ";base64," + encoder.encodeToString(matOfByte.toArray());
    }
}
