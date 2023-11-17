package ImageMatching;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class Tile {
    private String name;
    private Mat Img;

    private Point coor;

    /**
     * Constructor of Tile
     * @param name name of the tile
     * @param path path of the tile image file (ex: scr/img/dataSet/data1/bamboo_1.png)
     */
    public Tile(String name, String path){
        this.name=name;
        this.Img= Imgcodecs.imread(path);
    }

    public Tile(String name, Mat img){
        this.name=name;
        this.Img=img;
    }
    public void setName(String n){
        name=n;
    }

    public String getName() {
        return name;
    }

    public Mat getImg() {
        return Img;
    }


    public void setCoor(double x, double y){
        this.coor=new Point(x,y);
    }


    public Point getCoor(){
        return this.coor;
    }





}
