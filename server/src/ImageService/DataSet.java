package ImageService;

import ImageService.Tiles.ImageTile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.SIFT;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet {
    private static int MIN_SCORE = 100;
    private HashMap<ImageTile, Mat> tiles;
    private String name;
    private SIFT sift;

    protected DataSet() {
        this.tiles = new HashMap<>();
        this.sift = SIFT.create();
    }

    public DataSet(String name) {
        this.tiles = new HashMap<>();
        this.sift = SIFT.create();
        this.name = name;

        load(name);
    }

    protected void load(String dataName){
        loadFolder(dataName);
    }

    private void loadFromFile(String path) throws Exception {
        String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
        JSONObject json = new JSONObject(jsonContent);

    }
    private void loadFolder(String folderName) {
        File folder = new File("src/img/dataSet/" + folderName);
        File[] listOfFiles = folder.listFiles();
        MatOfKeyPoint keyPoints = new MatOfKeyPoint();

        for(int i = 0; i < listOfFiles.length; ++i){
            File file = listOfFiles[i];
            String fileName= file.getName();

            fileName = fileName.substring(0, fileName.lastIndexOf("."));

            ImageTile img = new ImageTile(fileName, file.toString());
            Mat des = new Mat();

            sift.detect(img.getImg(), keyPoints);
            sift.compute(img.getImg(), keyPoints, des);

            this.tiles.put(img, des);
        }
    }

    public void save(){
        this.save(this.name);
    }
    protected void save(String dataName){
        String path = "src/ImageService/Sifts/" + dataName + ".json";
        File siftFile = new File(path);

        try {
            siftFile.createNewFile();
        } catch (IOException e) {
            System.out.println(e);;
        }

    }
    public ImageTile findMatchingTile(Mat img, float ratioThreshold){
        ImageTile matchedTile = null;
        double bestScore = 0;

        MatOfKeyPoint kp1 = new MatOfKeyPoint();
        Mat des1 = new Mat();
        Mat des2;

        sift.detect(img, kp1);
        sift.compute(img, kp1, des1);
        BFMatcher matcher = BFMatcher.create();

        List<MatOfDMatch> knnMatches = new ArrayList<>();

        for(Map.Entry<ImageTile, Mat> tileInfo : this.tiles.entrySet()) {
            ImageTile currentTile = tileInfo.getKey();
            des2 = tileInfo.getValue();

            matcher.knnMatch(des1, des2, knnMatches, 2);

            double score = 0;
            for (MatOfDMatch knnMatch : knnMatches) {
                DMatch[] matches = knnMatch.toArray();
                if (matches[0].distance < ratioThreshold * matches[1].distance) {
                    score += ratioThreshold *matches[1].distance - matches[0].distance;
                }
            }
            if(bestScore < score && score > MIN_SCORE){
                bestScore = score;
                matchedTile = currentTile;
            }
        }

        return matchedTile;
    }

    public ImageTile findMatchingTile(Mat img){
        return findMatchingTile(img, .6f);
    }

    public void sizeTo(Size size){
        if(this.tiles.size() == 0) return;

        for(Map.Entry<ImageTile, Mat> tileInfo : this.tiles.entrySet()) {
            ImageTile img = tileInfo.getKey();
            if(img.getImg().cols() != size.width || img.getImg().rows() != size.height){
                rescaleImg(img.getImg(), (int)size.width, (int)size.height);
            }
        }
    }
    private void rescaleImg(Mat img, int newWidth, int newHeight){
        Size scaleSize = new Size(newWidth, newHeight);

        Imgproc.resize(img,img,scaleSize);
    }
}
