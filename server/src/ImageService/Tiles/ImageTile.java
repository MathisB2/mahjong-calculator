package ImageService.Tiles;

import Clustering.ClusterPoint;
import ImageService.ImageEncoder;
import org.json.JSONObject;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageTile extends ClusterPoint {
    final static public ImageTile NULL = new ImageTile(Mat.zeros(370, 280,CvType.CV_8UC3));
    private String name;
    private Mat img;
    public ImageTile(Mat img){
        this.name = "NULL";
        this.img = img;
    }
    public ImageTile(String name, String path){
        this.name = name;
        this.img = Imgcodecs.imread(path);
    }

    public ImageTile(String name, Mat img){
        this.name = name;
        this.img = img;
    }
    public String getName() {
        return name;
    }
    public Mat getImg() {
        return img;
    }

    @Override
    public String toString() {
        return "ImageTile{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        return ((ImageTile) obj).getName() == this.getName();
    }
    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("name", this.name);
            obj.put("image", (new ImageEncoder()).encode(this.img));
        }catch (Exception e){};

        return obj;
    }
}
