import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;

import static org.opencv.features2d.Features2d.*;

public class ImageDetection {

    private Mat uploadedImg;
    private ArrayList<Mat> extractedTiles=new ArrayList<Mat>();
    private static int targetX=1600;

    private ArrayList<MatOfPoint> contours=new ArrayList<MatOfPoint>();

    private HashMap<String,Mat> dataSet=new HashMap<String,Mat>();




    public void loadimage(String filePath){
        uploadedImg= Imgcodecs.imread(filePath);

        // rotate if needed
        if(uploadedImg.width()<uploadedImg.height()){
            Core.rotate(uploadedImg,uploadedImg,Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        // rescale image
        float scale=(float)targetX/uploadedImg.width();
        Size scaleSize=new Size(targetX, uploadedImg.height()*scale);
        Imgproc.resize(uploadedImg,uploadedImg,scaleSize);

    }

    public void getContour(){
        // convert to gray
        Mat gray = new Mat();
        Imgproc.cvtColor(uploadedImg, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(gray,gray,new Size(5,5));

        //Imgproc.adaptiveThreshold(gray, gray, 255,1, 1, 11, 2);
        Imgproc.threshold(gray,gray,0,255,Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);
        //Imgproc.Canny(gray,gray,85,255,3);

        // find contours
        Imgproc.findContours(gray, contours, gray, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

    }

    public void extractTiles(){

        for (MatOfPoint cnt : contours) {

            // transforme les points en rectangles
            MatOfPoint2f m2p= new MatOfPoint2f();
            double epsilon=0.1*Imgproc.arcLength(new MatOfPoint2f(cnt.toArray()),true);
            Imgproc.approxPolyDP(new MatOfPoint2f(cnt.toArray()),m2p,epsilon,true);

            MatOfPoint approx=new MatOfPoint();
            m2p.convertTo(approx,CvType.CV_32S);

            //Imgproc.drawContours(img, Arrays.asList(approx),0,new Scalar(0,0,255),2);

            ArrayList<Integer> four=new ArrayList<Integer>();

            int[] n=new int[(int)(approx.total()*approx.channels())];
            approx.get(0,0,n);
            // stocker les coordonnées des points
            for(int j=0;j<n.length;j+=2){
                four.add(n[j]);
                four.add(n[j+1]);
            }

            // taille cible
            int width=280;
            int height=370;
            //corriger distorsion de l'image
            Point[] inputPts=new Point[]{new Point(four.get(0),four.get(1)),new Point(four.get(6),four.get(7)),new Point(four.get(4),four.get(5)),new Point(four.get(2),four.get(3))};
            Point[] outputPts=new Point[]{new Point(0,0),new Point(width,0),new Point(width,height),new Point(0,height)};
            MatOfPoint2f src=new MatOfPoint2f(inputPts);
            MatOfPoint2f dst=new MatOfPoint2f(outputPts);

            Mat perspectivetransform=Imgproc.getPerspectiveTransform(src,dst);
            Mat finalImage=new Mat();
            Imgproc.warpPerspective(uploadedImg,finalImage,perspectivetransform,new Size(width,height));
            testTile(finalImage);
            // ajout des images dans un tableau
            extractedTiles.add(finalImage);
        }
    }

    /**
     *
     */
    public String testTile(Mat tile){
        int maxMatch=0;
        String tileName="";
        float ratioThreshold = .6f;
        boolean test=false;

        HighGui.imshow("Image", tile);
        HighGui.waitKey();
        // Create SIFT detector
        SIFT sift = SIFT.create();

        // Détecter les points clés et calculer les descripteurs de l'image reçu
        MatOfKeyPoint kp1 = new MatOfKeyPoint();
        Mat des1 = new Mat();
        sift.detectAndCompute(tile, new Mat(), kp1, des1);


        // Créer un objet BFMatcher
        BFMatcher matcher = BFMatcher.create();

        Mat imgMatches = new Mat();

        for(Map.Entry<String,Mat> entry : dataSet.entrySet()) {
            String key = entry.getKey();
            Mat value = entry.getValue();

            MatOfKeyPoint kp2 = new MatOfKeyPoint();
            Mat des2 = new Mat();
            sift.detectAndCompute(value, new Mat(), kp2, des2);

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
            System.out.println(key+"  "+goodMatchesList.size());

            if(maxMatch<goodMatchesList.size()){
                maxMatch=goodMatchesList.size();
                tileName=key;
                test=true;
                MatOfDMatch goodMatches = new MatOfDMatch();
                goodMatches.fromList(goodMatchesList);
                Features2d.drawMatches(tile, kp1, value, kp2, goodMatches, imgMatches, new Scalar(255, 0, 0),
                        new Scalar(0, 0, 255), new MatOfByte(), Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);
            }
        }

        System.out.println("la tuile trouvé est : "+tileName);;


        // Afficher le résultat
        if(test) {
            HighGui.imshow("Matches", imgMatches);
            HighGui.waitKey();
        }
        return tileName;
    }

    public void loadDataSet(String folderName){
        File folder =new File("src/img/dataSet/"+folderName);
        File[] listOfFiles=folder.listFiles();

        for(File file : listOfFiles){
            System.out.println(file.toString());
            dataSet.put(file.getName().substring(0,file.getName().length()-4),Imgcodecs.imread(file.toString()));
            System.out.println(file.getName());
        }
    }



}
