package ImageService;

import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.SIFT;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataSet {
    private String name;
    private ArrayList<ImageTile> tiles;

    public DataSet() {
        tiles = new ArrayList<>();
    }

    public DataSet(String name) {
        this.tiles = new ArrayList<>();
        this.name = name;
        loadFolder(name);
    }
    private void loadFolder(String folderName){
        if(this.tiles.size() != 0){
            System.err.println("dataSet "+folderName+" already loaded");
            return;
        }

        File folder = new File("src/img/dataSet/"+folderName);
        File[] listOfFiles = folder.listFiles();
        String fileName;

        for(File file : listOfFiles){
            fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));

            this.tiles.add(new ImageTile(fileName,file.toString()));
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

        for(ImageTile currentTile : this.tiles) {
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

        return new ImageTile("", Mat.zeros(0, 0, CvType.CV_8UC3));
    }
    public void sizeTo(Size size){
        if(this.tiles.size() == 0) return;

        for(ImageTile img : this.tiles){
            if(img.getImg().cols() != size.width || img.getImg().rows() != size.height){
                img.setImg(rescaleImg(img.getImg(), (int)size.width, (int)size.height));
            }
        }
    }
    private Mat rescaleImg(Mat img, int newWidth, int newHeight){
        Mat resized = new Mat();
        Size scaleSize=new Size(newWidth, newHeight);
        Imgproc.resize(img,resized,scaleSize);
        return resized;
    }
}
