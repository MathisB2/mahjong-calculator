package ImageMatching;

import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

import static org.opencv.features2d.Features2d.*;

public class TileDetector {


    private ArrayList<Tile> extractedTiles=new ArrayList<Tile>();

    private ArrayList<MatOfPoint> contours=new ArrayList<>();
    private DataSet dataSet;


    private static int targetX;
    private static int blurRadius;
    private Mat uploadedImg;



    /**
     * constructor of ImageDetector
     * @param resolution max width of image used for detection
     */
    public TileDetector(int resolution) {
        targetX=resolution;
        blurRadius=5;
        dataSet=new DataSet();
    }

    /**
     * load the image dataSet into an ArrayList
     * @param folderName name of the folder to be read (folder must exists in scr/img/dataSet/)
     */
    public void loadDataSet(String folderName){
        dataSet.loadFolder(folderName);
    }


    /**
     * load the image that will be used for extracting tiles
     * @param filePath path of the image
     */
    public void loadImage(String filePath){
        loadImage(Imgcodecs.imread(filePath));
    }

    /**
     * load the image that will be used for extracting tiles
     * @param img Mat image
     */
    public void loadImage(Mat img){
        uploadedImg= img;
        rescaleUploaded();
    }


    /**
     * compute contours of the loaded image. This step is necessary before extracting tiles
     */
    public void findContours(boolean simplify, int minArea){
        // convert to gray
        Mat gray = new Mat();
        Imgproc.cvtColor(uploadedImg, gray, Imgproc.COLOR_BGR2GRAY);
        //blur
        Imgproc.blur(gray,gray,new Size(blurRadius,blurRadius));

        Imgproc.threshold(gray,gray,0,255,Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);

        // find contours
        ArrayList<MatOfPoint> tmp=new ArrayList<>();
        Imgproc.findContours(gray, tmp, gray, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        for(MatOfPoint cnt:tmp){
            //ignore too small contours
            if (Imgproc.contourArea(cnt) >= minArea) {
                if(simplify){
                    //force contours to be quads
                    MatOfPoint quad=simplifyContour(cnt, 4);
                    if(quad.toArray().length==4) {
                        contours.add(quad);
                    }
                }else{
                    contours.add(cnt);
                }

            }
        }

    }

    public void findContours(){
        findContours(true,1000);
    }

    /**
     * show detected contours of the loaded image
     */
    public void showContours() {
        if (contours.size() == 0) {
            findContours();
        }
        Mat clone = uploadedImg.clone();

        int i;
        for (i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(clone, contours, i, new Scalar(0, 255, 255), 3);
        }
        HighGui.imshow("Contours", clone);
        HighGui.waitKey();
        System.out.println(i + " tiles detected");
    }


    /**
     * extract each tile of the base image and warp perspective. Results are stored in extractedTiles.
     */

    public void extractTiles(){
        for (MatOfPoint cnt : contours) {
            //perspective transform
            int width=280;
            int height=370;

            Point[] inputPts = sortContourPoints(cnt.toArray());;
            Point[] outputPts = new Point[]{new Point(0, 0), new Point(width, 0), new Point(width, height), new Point(0, height)};
            MatOfPoint2f src = new MatOfPoint2f(inputPts);
            MatOfPoint2f dst = new MatOfPoint2f(outputPts);

            Mat perspectivetransform=Imgproc.getPerspectiveTransform(src,dst);
            Mat finalImage=new Mat();
            Imgproc.warpPerspective(uploadedImg,finalImage,perspectivetransform,new Size(width,height));
            //testTile2(finalImage);
            // ajout des images dans un tableau

            Tile t=new Tile("",finalImage);

            Point center=getCenterPoint(inputPts);
            t.setCoor(center.x,center.y);
            extractedTiles.add(t);
        }

    }

    public void matchAllTiles(){
        for(Tile t:extractedTiles){
//            dataSet.findMatchingTile(t.getImg());
            t.setName(dataSet.findMatchingTile(t.getImg()).getName());
        }
    }
    public List<List<Tile>> findCluster(){
        List<List<Tile>> clusters=new ArrayList<>();
        for(int i=0;i<extractedTiles.size();i++){
            clusters.add(new ArrayList<>());
            clusters.get(i).add(extractedTiles.get(i));
        }
        int i1=0;
        int i2=0;
        double dist=-1;
        System.out.println("find cluster");
        while(clusters.size()>5){
            System.out.println("size :"+clusters.size());
            dist=-1;
            i1=0;
            i2=0;
            for(int i=0;i<clusters.size();i++){
                for(int y=i+1;y<clusters.size();y++){
                    double d=dMin(clusters.get(i),clusters.get(y));
                    if(dist==-1 || dist>d){
                        dist=d;
                        i1=i;
                        i2=y;
                    }

                }
            }
            clusters=concatList(clusters,i1,i2);

        }

        return clusters;
    }

    public List<List<Tile>> concatList(List<List<Tile>> liste, int i1, int i2){
        for(Tile t:liste.get(i2)){
            liste.get(i1).add(t);
        }
        liste.remove(i2);
        return liste;
    }
    public double dMin(List<Tile> l1,List<Tile>l2){
        double minDist=-1;
        for(int i=0;i<l1.size();i++){
            for(int y=0;y<l2.size();y++){
                double dist=calculateDistance(l1.get(i).getCoor(),l2.get(y).getCoor());
                if(minDist==-1 || dist<minDist){
                    minDist=dist;
                }

            }
        }
        return minDist;
    }



    /**
     * Rescale the image uploaded by loadImage()
     * Rotate if height>width
     * Then rescale to set width=targetX
     */
    private void rescaleUploaded(){
        // rotate if needed
        if(uploadedImg.width()<uploadedImg.height()){
            Core.rotate(uploadedImg,uploadedImg,Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        // rescale image
        float scale=(float)targetX/uploadedImg.width();
        Size scaleSize=new Size(targetX, uploadedImg.height()*scale);
        Imgproc.resize(uploadedImg,uploadedImg,scaleSize);
    }


    /**
     * simplify a MatOfPoint contour to match the number of corners given
     * @param cnt original contour
     * @param nCorners number of corners wanted
     * @return reshaped contour
     */
    private MatOfPoint simplifyContour(MatOfPoint cnt, int nCorners) {
        int nIter = 0;
        int maxIter = 100;
        double lb = 0.0;
        double ub = 1.0;

        while (nIter < maxIter) {
            nIter++;

            double k = (lb + ub) / 2.0;
            double eps = k * Imgproc.arcLength(new MatOfPoint2f(cnt.toArray()), true);

            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(cnt.toArray()), approx, eps, true);

            if (approx.total() > nCorners) {
                lb = (lb + ub) / 2.0;
            } else if (approx.total() < nCorners) {
                ub = (lb + ub) / 2.0;
            } else {
                return new MatOfPoint(approx.toArray());
            }
        }
        return cnt;
    }


    private Point[] sortContourPoints(Point[] points){
        return sortContourPoints(points, true);
    }

    /**
     * sort contours points to make them (counter) clockwise and avoid mirror tiles
     * @param points points of the contour
     * @param clockwise boolean to set sort method (true=clockwise)
     * @return sorted points
     */
    private Point[] sortContourPoints(Point[] points, boolean clockwise) {

        Point pointA = findClosestPoint(points, new Point(0, 0));

        points = removePoint(points, pointA);
        Point pointB = findClosestPoint(points, pointA);

        points = removePoint(points, pointB);
        Point pointC = findClosestPoint(points, pointB);

        points = removePoint(points, pointC);
        Point pointD = findClosestPoint(points, pointC);

        Point[] ret=new Point[]{pointA, pointB, pointC, pointD};
        if(!isClockwise(ret)){
            ret= invertPoints(ret,0,1);
            ret= invertPoints(ret,2,3);
        }

        if(clockwise){
            return ret;
        }else{
            return invertPoints(ret,1,3);
        }
    }


    /**
     * inverts two points of an array
     * @param array input array with size>2
     * @param index1 index of the first value to invert
     * @param index2 index of the second value
     * @return inverted array
     */
    private Point[] invertPoints(Point[] array, int index1, int index2){
        Point p=array[index1];
        array[index1]=array[index2];
        array[index2]=p;
        return array;
    }

    /**
     * check if a polygon (list of points) is sorted clockwise
     * @param points point list of the polygon
     * @return boolean : true if the polygon is sorted clockwise
     */
    private boolean isClockwise(Point[] points) {
        int numPoints = points.length;

        if (numPoints < 3) {
            return false;
        }

        double sum = 0;
        for (int i = 0; i < numPoints; i++) {
            Point currentPoint = points[i];
            Point nextPoint = points[(i + 1) % numPoints];
            sum += (nextPoint.x - currentPoint.x) * (nextPoint.y + currentPoint.y);
        }
        return sum < 0;
    }



    private Point findClosestPoint(Point[] points, Point reference) {
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


    private Point[] removePoint(Point[] points, Point remove) {
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


    private double calculateDistance(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
    }


    /**
     * calculates the center of mass of the given points
     * @param points the list of points
     * @return the center of mass of the points
     */
    private Point getCenterPoint(Point[] points) {
        int numPoints = points.length;

        if (numPoints == 0) {
            return new Point(0, 0);
        }

        double sumX = 0;
        double sumY = 0;

        for (Point point : points) {
            sumX += point.x;
            sumY += point.y;
        }

        double meanX = sumX / numPoints;
        double meanY = sumY / numPoints;

        return new Point(meanX, meanY);
    }

}
