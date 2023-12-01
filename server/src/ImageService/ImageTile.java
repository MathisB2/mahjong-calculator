package ImageService;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageTile {
    private String name;
    private Mat img;
    private Point coor;

    public ImageTile(String name, String path){
        this.name = name;
        this.img = Imgcodecs.imread(path);
    }
    public ImageTile(String name, Mat img){
        this.name=name;
        this.img=img;
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
        this.img=img;
    }
    public void setCoor(double x, double y){
        this.coor=new Point(x,y);
    }
    public void setCoor(Point p){
        this.coor=p;
    }
    public Point getCoor(){
        return this.coor;
    }
}
