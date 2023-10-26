
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Imgproc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    void pointImage(){

    }
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // largeur cible
        int targetX=1600;
        // load image before rescale
        Mat img = Imgcodecs.imread("src/render6.png");

        if(img.width()<img.height()){
            Core.rotate(img,img,Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        float scale=(float)targetX/img.width();
        Size scaleSize=new Size(targetX, img.height()*scale);
        Imgproc.resize(img,img,scaleSize);


        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(gray,gray,new Size(5,5));

        //Imgproc.adaptiveThreshold(gray, gray, 255,1, 1, 11, 2);
        Imgproc.threshold(gray,gray,0,255,Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_OTSU);
        //Imgproc.Canny(gray,gray,85,255,3);
        HighGui.imshow("Shapes", gray);
        HighGui.waitKey();
        HighGui.destroyAllWindows();
        // find contours

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(gray, contours, gray, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        int nbContours=0;
        for (MatOfPoint cnt : contours) {
            //System.out.println("test");
            Imgproc.drawContours(img, contours, contours.indexOf(cnt), new Scalar(0, 255, 255), 3);
        }

        // show
        HighGui.imshow("Shapes", img);
        HighGui.waitKey();
        HighGui.destroyAllWindows();


        /*
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat img = Imgcodecs.imread("src/b.jpg");

        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

        Mat thresh = new Mat();
        Imgproc.threshold(gray, thresh, 50, 255, Imgproc.THRESH_BINARY);


        Mat hierarchy = new Mat();

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println("Number of contours detected: " + contours.size());

        for (MatOfPoint cnt : contours) {
            int x1 = (int) cnt.get(0, 0)[0];
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(cnt.toArray()), approx, 0.01 * Imgproc.arcLength(new MatOfPoint2f(cnt.toArray()), true), true);

            if (approx.rows() == 4) {
                Rect rect = Imgproc.boundingRect(cnt);
                int x = rect.x;
                int y = rect.y;
                int w = rect.width;
                int h = rect.height;

                double ratio = (double) w / h;

                if (ratio >= 0.9 && ratio <= 1.1) {
                    Imgproc.drawContours(img, contours, contours.indexOf(cnt), new Scalar(0, 255, 255), 3);
                    Imgproc.putText(img, "Square", new Point(x1, y), Core.SORT_DESCENDING, 0.6, new Scalar(255, 255, 0), 2);
                } else {
                    Imgproc.putText(img, "Rectangle", new Point(x1, y), Core.SORT_DESCENDING, 0.6, new Scalar(0, 255, 0), 2);
                    Imgproc.drawContours(img, contours, contours.indexOf(cnt), new Scalar(0, 255, 0), 3);
                }
            }
        }

        HighGui.imshow("Shapes", img);
        HighGui.waitKey();
        HighGui.destroyAllWindows();
    */
        /*
        // Charger la bibliothèque OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Charger l'image depuis le chemin spécifié
        String imagePath = "src/render5.png";
        Mat image = Imgcodecs.imread(imagePath);

        if (image.empty()) {
            System.out.println("Impossible de charger l'image.");
            return;
        }

        // Convertir l'image en niveaux de gris
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Appliquer un flou gaussien pour réduire le bruit
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);

        // Détecter les bords de l'image en utilisant l'algorithme de Canny
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 100, 200);

        // Trouver les contours dans l'image
        Mat hierarchy = new Mat();
        java.util.List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, (java.util.List<MatOfPoint>) contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Parcourir les contours et filtrer les rectangles
        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

            double epsilon = 0.02 * Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approxCurve, epsilon, true);

            if (approxCurve.total() == 4) {
                // Le contour approximé a 4 côtés, c'est un rectangle
                Imgproc.drawContours(image, Collections.singletonList(contour), -1, new Scalar(0, 0, 255), 2);
            }
        }

        // Afficher l'image avec les rectangles encadrés
        HighGui.imshow("Image avec rectangles", image);
        HighGui.waitKey();

        // Fermer toutes les fenêtres
        HighGui.destroyAllWindows();
        */

    }

}