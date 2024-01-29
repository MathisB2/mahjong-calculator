package ImageService;

import Clustering.ClusterPoint;
import org.json.JSONObject;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageTile extends ClusterPoint {
    static public ImageTile NULL = new ImageTile("",Mat.zeros(370, 280,CvType.CV_8UC3));
    private String name;
    private Mat img;

    public ImageTile(String name, String path){
        this.name = name;
        this.img = Imgcodecs.imread(path);
    }
    public ImageTile(String name, Mat img){
        this.name = name;
        this.img = img;
    }
    public void setName(String n){
        name=n;
    }
    public String getName() {
        return name;
    }
    public Mat getImg() {
        return img;
    }
    public void setImg(Mat img){
        this.img = img;
    }
    @Override
    public String toString() {
        return this.toJSONObject().toString();
    }

    @Override
    public boolean equals(Object obj){
        return ((ImageTile) obj).getName() == this.getName();
    }
    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("name", this.name);
        }catch (Exception e){};

        return obj;
    }
}
