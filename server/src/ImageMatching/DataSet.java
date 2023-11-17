package ImageMatching;

import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SIFT;
import org.opencv.highgui.HighGui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.opencv.features2d.Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS;

public class DataSet {
    private String name;
    private ArrayList<Tile> dataSet;

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

    public Tile getTile(int i){
        return dataSet.get(i);
    }

    public ArrayList<Tile> toArray(){
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
                name=file.getName();
                pos=name.lastIndexOf(".");
                name=name.substring(0,pos);
                dataSet.add(new Tile(name,file.toString()));
            }
        }else{
            System.out.println("dataSet "+folderName+" already loaded");
        }
    }


    public Tile findMatchingTile(Mat img){

        int maxMatch=0;
        Tile matchedTile=dataSet.get(0);
        float ratioThreshold = .6f;

        //HighGui.imshow("Image", img);
        //HighGui.waitKey();


        // Create SIFT detector
        SIFT sift = SIFT.create();

        //Compute keypoints and desciptors of given image
        MatOfKeyPoint kp1 = new MatOfKeyPoint();
        Mat des1 = new Mat();
        sift.detectAndCompute(img, new Mat(), kp1, des1);

        //Create a BFMatcher object
        BFMatcher matcher = BFMatcher.create();

        Mat imgMatches = new Mat();

        for(Tile currentTile:dataSet) {

            MatOfKeyPoint kp2 = new MatOfKeyPoint();
            Mat des2 = new Mat();
            sift.detectAndCompute(currentTile.getImg(), new Mat(), kp2, des2);

            // Effectuer la correspondance
            List<MatOfDMatch> knnMatches = new ArrayList<>();
            matcher.knnMatch(des1, des2, knnMatches, 2);

            // Appliquer le test de rapport
            LinkedList<DMatch> goodMatchesList = new LinkedList<>();

            for (MatOfDMatch knnMatch : knnMatches) {
                DMatch[] matches = knnMatch.toArray();
                if (matches[0].distance < ratioThreshold * matches[1].distance) {
                    goodMatchesList.addLast(matches[0]);
                }
            }
            System.out.println(currentTile.getName()+"  "+goodMatchesList.size());

            if(maxMatch<goodMatchesList.size()){
                maxMatch=goodMatchesList.size();
                matchedTile= currentTile;
                MatOfDMatch goodMatches = new MatOfDMatch();
                goodMatches.fromList(goodMatchesList);
                Features2d.drawMatches(img, kp1, currentTile.getImg(), kp2, goodMatches, imgMatches, new Scalar(255, 0, 0),
                        new Scalar(0, 0, 255), new MatOfByte(), DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);
            }
        }

        System.out.println("la tuile trouvé est : "+matchedTile.getName());;


        if(!imgMatches.empty()) {
            // Afficher le résultat
            //HighGui.imshow("Matches", imgMatches);
            //HighGui.waitKey();
            return matchedTile;
        }

        return null;
    }
}
