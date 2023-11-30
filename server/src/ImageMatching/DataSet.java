package ImageMatching;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SIFT;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.opencv.features2d.Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS;

public class DataSet {
    private String name;
    private ArrayList<ImageTile> dataSet;

    public DataSet() {
        dataSet=new ArrayList<>();
    }

    public DataSet(String name) {
        this.name = name;
        dataSet=new ArrayList<>();
        loadFolder(name);
    }

    public String getName() {
        return name;
    }

    public int getSize(){
        return dataSet.size();
    }

    public ImageTile getTile(int i){
        return dataSet.get(i);
    }

    public ArrayList<ImageTile> toArray(){
        return dataSet;
    }

    public void loadFolder(String folderName){
        if(!folderName.equals(name)){
            System.out.println("loading dataSet "+folderName);
            name=folderName;
            File folder =new File("src/img/dataSet/"+folderName);
            File[] listOfFiles=folder.listFiles();
            String name;
            int pos;

            for(File file : listOfFiles){
                name = file.getName();
                pos = name.lastIndexOf(".");
                name = name.substring(0,pos);
                dataSet.add(new ImageTile(name,file.toString()));
            }
        }else{
            System.out.println("dataSet "+folderName+" already loaded");
        }
    }


    public ImageTile findMatchingTile(Mat img){
        ImageTile matchedTile = null;
        float ratioThreshold = .6f;
        double bestScore = 0;

        SIFT sift = SIFT.create();

        MatOfKeyPoint kp1 = new MatOfKeyPoint();
        Mat des1 = new Mat();
        Mat des2 = new Mat();

        sift.detect(img, kp1);
        sift.compute(img, kp1, des1);

        BFMatcher matcher = BFMatcher.create();
        MatOfKeyPoint kp2 = new MatOfKeyPoint();

        List<MatOfDMatch> knnMatches = new ArrayList<>();

        for(ImageTile currentTile : dataSet) {
            sift.detect(currentTile.getImg(), kp2);
            sift.compute(currentTile.getImg(), kp2, des2);
            matcher.knnMatch(des1, des2, knnMatches, 2);

            double score = 0;
            for (MatOfDMatch knnMatch : knnMatches) {
                DMatch[] matches = knnMatch.toArray();
                if (matches[0].distance < ratioThreshold * matches[1].distance) {
                    score += ratioThreshold *matches[1].distance - matches[0].distance;
                }
            }

            if(bestScore < score){
                bestScore = score;
                matchedTile = currentTile;
            }
        }

        if(matchedTile != null) {
            System.out.println("la tuile trouvée est : "+matchedTile.getName());
            return matchedTile;
        }

        return new ImageTile("",getEmptyImg());
    }

    private Mat rescaleImg(Mat img, int newWidth, int newHeight){
        Mat resized = new Mat();
        Size scaleSize=new Size(newWidth, newHeight);
        Imgproc.resize(img,resized,scaleSize);
        return resized;
    }

    public void setSize(int width, int height){
        if(this.dataSet.size()>0){
            for(ImageTile img:dataSet){
                if(img.getImg().cols()!=width || img.getImg().rows()!=height){
                    img.setImg(rescaleImg(img.getImg(),width,height));
                }
            }
        }
    }

    private Mat getEmptyImg(){
        return Mat.zeros(0, 0, CvType.CV_8UC3);
    }
}
