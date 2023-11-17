import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class OpenCVTest {
    public static int getMainColor(Mat img) {
        return getMainColor(img, false);
    }

    public static int getMainColor(Mat img, boolean ignoreZeros) {
        if (img.channels() != 1) {
            throw new IllegalArgumentException("input image must have only one chanel");
        }
        //image must be gray
        double moy = 0;
        int count = 0;

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                if (!ignoreZeros || img.get(y, x)[0] != 0) {
                    moy += img.get(y, x)[0];
                    count++;
                }

            }
        }
        return (int) (moy / count);
    }


    public static int getRecomendedTreshold(Mat img) {
        return getRecomendedTreshold(img, false);
    }

    public static int getRecomendedTreshold(Mat img, boolean ingoreZeros) {
        return getMainColor(img, ingoreZeros) + 20;
    }


    public static double mapRange(double value, double fromMin, double fromMax, double toMin, double toMax) {
        if (fromMin == fromMax) {
            throw new IllegalArgumentException("fromMin and fromMax can't be the same value");
        }

        return (value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin;
    }

    public static Mat normalize(Mat img) {
        double min = 255;
        double max = 0;

        //trouver le min et le max parmi tous les chanels
        //logiquement min=treshold
        // appeler mapRange(img.get(y,x),min,max,0,255)


        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                for (double chanel : img.get(y, x)) {
                    if (chanel < min) {
                        min = chanel;
                    }
                    if (chanel > max) {
                        max = chanel;
                    }
                }
            }
        }
        Mat normalized = img.clone();
        int ch = img.channels();
        double[] tmp = new double[ch];

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                for (int i = 0; i < ch; i++) {
                    tmp[i] = mapRange(img.get(y, x)[i], min, max, 0, 255);
                }
                normalized.put(y, x, tmp);
            }
        }


        System.out.println("min : " + min);
        System.out.println("max : " + max);
        return normalized;
    }


    public static Mat replaceColorUnderTreshold(Mat img, int treshold) {

        Mat gray = img.clone();
        if (img.channels() != 1) {
            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);
        }


        Mat replaced = img.clone();
        double[] tmp = {treshold, treshold, treshold};

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                if (gray.get(y, x)[0] <= treshold) {
                    replaced.put(y, x, tmp);
                }

            }
        }
        return replaced;
    }


    public static Mat getTilesMask(Mat img, int treshold) {

        Mat gray = img.clone();
        if (img.channels() != 1) {
            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);
        }


        Mat replaced = img.clone();
        double[] tmp = {treshold, treshold, treshold};

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                if (gray.get(y, x)[0] <= treshold) {
                    replaced.put(y, x, tmp);
                }

            }
        }
        return replaced;
    }


    public static MatOfPoint simplifyContour(MatOfPoint contour, int nCorners) {
        int nIter = 0;
        int maxIter = 100;
        double lb = 0.0;
        double ub = 1.0;

        while (nIter < maxIter) {
            nIter++;

            double k = (lb + ub) / 2.0;
            double eps = k * Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true);

            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()), approx, eps, true);

            if (approx.total() > nCorners) {
                lb = (lb + ub) / 2.0;
            } else if (approx.total() < nCorners) {
                ub = (lb + ub) / 2.0;
            } else {
                return new MatOfPoint(approx.toArray());
            }
        }
        return contour;
    }


    public static Point[] sortContourPoints(Point[] points) {
        Point pointA = findClosestPoint(points, new Point(0, 0));

        points = removePoint(points, pointA);
        Point pointB = findClosestPoint(points, pointA);

        points = removePoint(points, pointB);
        Point pointC = findClosestPoint(points, pointB);

        points = removePoint(points, pointC);
        Point pointD = findClosestPoint(points, pointC);

        return new Point[]{pointA, pointB, pointC, pointD};
    }

    private static Point findClosestPoint(Point[] points, Point reference) {
        double minDistance = calculateDistance(points[0], reference);
        Point closest = points[0];
        for (Point point : points) {
            double distance = calculateDistance(point, reference);
            if (distance < minDistance) {
                minDistance = distance;
                closest = point;
            }
        }
        return closest;
    }


    private static Point[] removePoint(Point[] points, Point remove) {
        ArrayList<Point> removed = new ArrayList<>();
        for (Point point : points) {
            if (!point.equals(remove)) {
                removed.add(point);
            }
        }
        Point[] ret = new Point[removed.size()];
        for (int i = 0; i < removed.size(); i++) {
            ret[i] = removed.get(i);
        }
        return ret;
    }


    public static double calculateDistance(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
    }


    public static void main(String[] args) {



        /*
        résumé de l'algo:
        - redimensionner l'image : 1600px pour le plus grand coté
        - passage en nuances de gris
        - flou : 3px
        - création d'un masque approximatif indiquant la position des tuiles :
        ----- treshold otsu (seuil global auto)
        ----- dilatation : 10px (pour étendre la zone du masque)
        - suppression des pixels en dehors du masque
        - détection des tuiles :
        ----- adaptative treshold (seuil auto par section)
        ----- errosion + median filter pour supprimer le grain
        - extraction des tuiles :
        ----- findContours
        ----- filtre sur l'aire (ingore aire<1000)
        ----- approximation en quadrilatère
        ----- ordonnement des contours
        ----- correction de perspective

         */

        int rescaleTarget = 1600;
        int blurRadius = 3;

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat original = Imgcodecs.imread("src/img/render3.png");

        if (original.width() < original.height()) {
            Core.rotate(original, original, Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        //rescale image
        float scale = (float) rescaleTarget / original.width();
        Size scaleSize = new Size(rescaleTarget, original.height() * scale);
        Imgproc.resize(original, original, scaleSize);


        Mat img = original.clone();
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(img, img, new Size(blurRadius, blurRadius));

        //System.out.println("Recomended treshold : "+getRecomendedTreshold(img));


//        Imgproc.threshold(img,img,getRecomendedTreshold(img),1,Imgproc.THRESH_BINARY);
        Imgproc.threshold(img, img, 0, 1, Imgproc.THRESH_OTSU);


//        Imgproc.erode(img,img,new Size(4,4));

        int dilateSize = 10;
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateSize, dilateSize));
        //Applying erosion on the Image
        Mat tilesMask = new Mat();
        Imgproc.dilate(img, tilesMask, kernel);

        //convert tileMask to BGR
        Mat maskBGR = new Mat();
        List<Mat> listMat = Arrays.asList(tilesMask, tilesMask, tilesMask);
        Core.merge(listMat, maskBGR);

        //remove pixels out of the mask
        Core.multiply(original, maskBGR, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

        HighGui.imshow("Base", img);
        HighGui.waitKey();
        HighGui.destroyAllWindows();

        //apply adaptative treshold
        Imgproc.adaptiveThreshold(img, img, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 7, 4);


        //Applying erosion on the Image
        dilateSize = 4;
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateSize, dilateSize));

        Imgproc.erode(img, img, kernel);
//        Imgproc.medianBlur(img,img,3);
//        Imgproc.medianBlur(img,img,3);


        HighGui.imshow("Base", img);
        HighGui.waitKey();
        HighGui.destroyAllWindows();


        Core.multiply(img, tilesMask, img);

        HighGui.imshow("Base", img);
        HighGui.waitKey();
        HighGui.destroyAllWindows();


        //find contours


        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(img, contours, img, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int count = 0;

        System.out.println(contours.size());
        for (MatOfPoint cnt : contours) {
            //filtrer les contours
            if (Imgproc.contourArea(cnt) >= 1000) {
                MatOfPoint simplifiedContour = simplifyContour(cnt, 4);

                List<MatOfPoint> simplifiedContoursList = new ArrayList<>();


                //afficher les contours
                simplifiedContoursList.add(simplifiedContour);

                Imgproc.drawContours(original, simplifiedContoursList, 0, new Scalar(0, 255, 255), 3);
                count++;


                Point[] points = simplifiedContour.toArray();
                for (Point point : points) {
                    System.out.println("Coordonnées du coin : (" + point.x + ", " + point.y + ")");

                }

                System.out.println();

                points = sortContourPoints(points);
                for (Point point : points) {
                    System.out.println("Coordonnées du coin : (" + point.x + ", " + point.y + ")");
//                    System.out.println(calculateDistance(point,new Point(0,0)));
                }

                System.out.println();
                System.out.println();


                //perspective transform
                int width = 250;
                int height = 350;
                Point[] inputPts = new Point[]{points[0], points[3], points[2], points[1]};
                Point[] outputPts = new Point[]{new Point(0, 0), new Point(width, 0), new Point(width, height), new Point(0, height)};
                MatOfPoint2f src = new MatOfPoint2f(points);
                MatOfPoint2f dst = new MatOfPoint2f(outputPts);


                Mat perspectivetransform = Imgproc.getPerspectiveTransform(src, dst);
                Mat finalImage = new Mat();
                Imgproc.warpPerspective(original, finalImage, perspectivetransform, new Size(250, 350));


                HighGui.imshow("Shapes", finalImage);
                HighGui.waitKey();
                HighGui.destroyAllWindows();
            }


        }

        System.out.println("Contour number : " + count);

        // show
        HighGui.imshow("Shapes", original);
        HighGui.waitKey();
        HighGui.destroyAllWindows();



/*
        System.out.println(img.channels());
        Mat replaced=replaceColorUnderTreshold(img,getRecomendedTreshold(img));




//        HighGui.imshow("Replaced", replaced);
//        HighGui.waitKey();
//        HighGui.destroyAllWindows();


        Mat normalized= normalize(replaced);
        HighGui.imshow("Normalized", normalized);
        HighGui.waitKey();
        HighGui.destroyAllWindows();


        Mat result= new Mat();
        System.out.println(getRecomendedTreshold(normalized,true));
        Imgproc.threshold(normalized,result,getRecomendedTreshold(normalized,true),255,Imgproc.THRESH_BINARY);

        HighGui.imshow("Normalized", result);
        HighGui.waitKey();
        HighGui.destroyAllWindows();







        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(result, contours, result, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //Imgproc.drawContours(original, contours,0,new Scalar(0,0,255),1);

        for (MatOfPoint cnt : contours) {

            //tracer le rotated bounding rect et utiliser les 4 points les plus proches parmi chaque polygone fermé
            //essayer dilate ou errode
            //filtrer en fonction de l'aire des contours

            Imgproc.drawContours(original, contours, contours.indexOf(cnt), new Scalar(255, 0, 0), 2);



        }

        HighGui.imshow("Normalized", original);
        HighGui.waitKey();
        HighGui.destroyAllWindows();

 */
    }


}
