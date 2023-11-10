import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class test {
    public static <DMatchVector> void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ImageDetection detect=new ImageDetection();
        detect.loadDataSet("data0");
        detect.loadimage("src/img/render6.png");

        detect.getContour();
        detect.extractTiles();





    }

        /*
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread("src/img/dataSet/data0/0026.jpg");
        Mat template = Imgcodecs.imread("src/img/dataSet/data0/0026.jpg");

        // Afficher les images
        HighGui.imshow("Image", img);
        HighGui.imshow("Template", template);

        // Créer l'objet SIFT
        SIFT sift = SIFT.create();

        // Détecter les points clés et calculer les descripteurs
        MatOfKeyPoint kp1 = new MatOfKeyPoint();
        Mat des1 = new Mat();
        sift.detectAndCompute(img, new Mat(), kp1, des1);

        MatOfKeyPoint kp2 = new MatOfKeyPoint();
        Mat des2 = new Mat();
        sift.detectAndCompute(template, new Mat(), kp2, des2);

        // Créer un objet BFMatcher
        BFMatcher matcher = BFMatcher.create();

        // Effectuer la correspondance
        List<MatOfDMatch> knnMatches = new ArrayList<>();
        matcher.knnMatch(des1, des2, knnMatches, 2);

        // Appliquer le test de rapport
        LinkedList<DMatch> goodMatchesList = new LinkedList<>();

        float ratioThreshold = 0.5f;
        for (MatOfDMatch knnMatch : knnMatches) {
            DMatch[] matches = knnMatch.toArray();
            if (matches[0].distance < ratioThreshold * matches[1].distance) {
                goodMatchesList.addLast(matches[0]);
            }
        }
        System.out.println(goodMatchesList.size());

        // Dessiner les correspondances
        Mat imgMatches = new Mat();
        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(goodMatchesList);
        Features2d.drawMatches(img, kp1, template, kp2, goodMatches, imgMatches, new Scalar(255, 0, 0),
                new Scalar(0, 0, 255), new MatOfByte(), Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);

        // Afficher le résultat
        HighGui.imshow("Matches", imgMatches);
        HighGui.waitKey();

        // Afficher le temps
        System.out.println("Time: " + System.currentTimeMillis());
    }*/
}
