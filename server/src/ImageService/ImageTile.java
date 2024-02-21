package ImageService;

import Clustering.ClusterPoint;
import org.json.JSONObject;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


/**
 * Class used for storing Tile data extracted from the image
 */
public class ImageTile extends ClusterPoint {
    final static public ImageTile NULL = new ImageTile(Mat.zeros(370, 280,CvType.CV_8UC3));
    private String name;
    private Mat img;

    /**
     * Constructor of ImageTile
     * The name will be set to "NULL"
     * @param img the Mat image of the tile
     */
    public ImageTile(Mat img){
        this.name = "NULL";
        this.img = img;
    }

    /**
     * Constructor of ImageTile
     * @param name the name of the tile
     * @param path the path to the image file
     */
    public ImageTile(String name, String path){
        this.name = name;
        this.img = Imgcodecs.imread(path);
    }

    /**
     * Constructor of ImageTile
     * @param name the name of the tile
     * @param img the Mat image of the tile
     */
    public ImageTile(String name, Mat img){
        this.name = name;
        this.img = img;
    }

    /**
     * Getter on the Name attribute
     * @return the name of the tile
     */
    public String getName() {
        return name;
    }

    /**
     * Getter on the img attribute
     * @return the Mat image of the tile
     */
    public Mat getImg() {
        return img;
    }
    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    @Override
    public boolean equals(Object obj){
        return ((ImageTile) obj).getName() == this.getName();
    }

    /**
     * Function to get the JSON object of the instance.
     * The JSON contains 2 fields :
     * - name : the name of the tile
     * - image : the image of the tile (base64 encoded)
     * @return JSON object of this
     */
    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("name", this.name);
            obj.put("image", (new ImageEncoder()).encode(this.img));
        }catch (Exception e){};

        return obj;
    }
}
