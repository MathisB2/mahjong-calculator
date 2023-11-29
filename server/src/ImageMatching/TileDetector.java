package ImageMatching;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class TileDetector {


    private ArrayList<ImageTile> extractedTiles=new ArrayList<>();
    private ArrayList<ImageTile> matchedTiles=new ArrayList<>();

    private ArrayList<MatOfPoint> contours=new ArrayList<>();
    private DataSet dataSet;


    private static int targetX;
    private static int blurRadius;
    private Mat uploadedImg;

    private int tileWidth;
    private int tileHeight;





    /**
     * constructor of ImageDetector
     * @param resolution max width of image used for detection
     */
    public TileDetector(int resolution) {
        targetX=resolution;
        blurRadius=5;
        dataSet=new DataSet();
        tileWidth=280;
        tileHeight=370;
    }

    /**
     * Global function to perform full image detection process
     * @param imagePath the path to the input image containing the tiles
     * @param dataSet the name of the dataSet to use
     * @param preview true means results will be displayed as images
     */
    public void runOn(String imagePath, String dataSet,boolean preview){
        runOn(Imgcodecs.imread(imagePath),dataSet,preview);
    }

    /**
     * Global function to perform full image detection process
     * @param image the input image containing the tiles
     * @param dataSet the name of the dataSet to use
     * @param preview true means results will be displayed as images
     */
    public void runOn(Mat image, String dataSet,boolean preview){
        loadDataSet(dataSet);
        loadImage(image);
        findContours();
        if(preview){
            showContours();
        }
        extractTiles();
        matchAllTiles();
        if(preview){
            showMatches(1800);
        }

        //find clusters
        clearData();
    }

    /**
     * reset all values to default in order to run detector again on other images
     */
    public void clearData(){
        extractedTiles=new ArrayList<>();
        matchedTiles=new ArrayList<>();
        contours=new ArrayList<>();
        uploadedImg=new Mat();
    }

    /**
     * load the image dataSet into an ArrayList
     * @param folderName name of the folder to be read (folder must exists in scr/img/dataSet/)
     */
    public void loadDataSet(String folderName){
        dataSet.loadFolder(folderName);
        dataSet.setSize(tileWidth,tileHeight);
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
        Mat clone = new Mat();
        Imgproc.cvtColor(uploadedImg, clone, Imgproc.COLOR_BGR2GRAY);
        //blur
        Imgproc.blur(clone,clone,new Size(blurRadius,blurRadius));

        Imgproc.threshold(clone, clone, 0, 1, Imgproc.THRESH_OTSU);

        int dilateSize = 10;
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateSize, dilateSize));
        //Applying erosion on the Image
        Mat tilesMask = new Mat();
        Imgproc.dilate(clone, tilesMask, kernel);

        //convert tileMask to BGR
        Mat maskBGR = new Mat();
        List<Mat> listMat = Arrays.asList(tilesMask, tilesMask, tilesMask);
        Core.merge(listMat, maskBGR);

        //remove pixels out of the mask
        Core.multiply(uploadedImg, maskBGR, clone);
        Imgproc.cvtColor(clone, clone, Imgproc.COLOR_BGR2GRAY);

        //apply adaptative treshold
        Imgproc.adaptiveThreshold(clone, clone, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 7, 4);

        //Apply erosion on the Image
        dilateSize = 4;
        kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateSize, dilateSize));

        Imgproc.erode(clone, clone, kernel);
//        Imgproc.medianBlur(img,img,3);

        Core.multiply(clone, tilesMask, clone);


        // find contours
        ArrayList<MatOfPoint> tmp=new ArrayList<>();
        Imgproc.findContours(clone, tmp, clone, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


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

            Point[] inputPts = sortContourPoints(cnt.toArray());
            Point[] outputPts = new Point[]{new Point(0, 0), new Point(tileWidth, 0), new Point(tileWidth, tileHeight), new Point(0, tileHeight)};
            MatOfPoint2f src = new MatOfPoint2f(inputPts);
            MatOfPoint2f dst = new MatOfPoint2f(outputPts);

            Mat perspectivetransform=Imgproc.getPerspectiveTransform(src,dst);
            Mat finalImage=new Mat();
            Imgproc.warpPerspective(uploadedImg,finalImage,perspectivetransform,new Size(tileWidth,tileHeight));
            //testTile2(finalImage);
            // ajout des images dans un tableau

            ImageTile t=new ImageTile("",finalImage);

            Point center=getCenterPoint(inputPts);
            t.setCoor(center.x,center.y);
            extractedTiles.add(t);
        }

    }

    public void matchAllTiles0(){
        for(int i=0;i<extractedTiles.size();i++){
            ImageTile t=extractedTiles.get(i);

            HighGui.imshow("t", t.getImg());
            HighGui.waitKey();
            ImageTile result=dataSet.findMatchingTile(t.getImg());

//
//            if(!result.getName().equals("")){
//                result.setCoor(t.getCoor());
//                System.out.println(result.getCoor());
//                matchedTiles.add(result);
//            }
            result.setCoor(t.getCoor());
            System.out.println(result.getCoor());
            matchedTiles.add(result);
            extractedTiles.get(i).setName(result.getName());


        }
    }

    public void matchAllTiles(){
        ArrayList<Thread> ths = new ArrayList<>();
        for(int i=0;i<extractedTiles.size();i++){
            ImageTile t = extractedTiles.get(i);

            Thread th = new Thread(() ->  {
                ImageTile result=dataSet.findMatchingTile(t.getImg());
                t.setName(result.getName());
                result.setCoor(t.getCoor());
                extractedTiles.add(t);

                if(!t.getName().equals("")){
                    matchedTiles.add(result);
                }


                System.out.println(t.getCoor());
            });
            th.start();
            ths.add(th);
        }
        extractedTiles.clear();
        try{
            for(Thread th : ths){
                th.join();
            }
        }catch(Exception e){}

        System.out.println(matchedTiles.size());
    }




    public void showMatches(int width){
        int borderWidth=10;
        int s=extractedTiles.size();
        Mat[][] binds=new Mat[2][s];
        Mat tmp;

        int j=0;
        for (int i = 0; i < s; i++) {
            tmp=extractedTiles.get(i).getImg();
            Imgproc.putText(tmp, extractedTiles.get(i).getName(), new Point(10,40), Core.SORT_DESCENDING, 1.2, new Scalar(0, 0, 255), 2);
            binds[0][i]=addRightBorder(tmp,borderWidth);
            if(extractedTiles.get(i).getName().equals("")){
                binds[1][i]= addRightBorder(Mat.zeros(new Size(tileWidth, tileHeight), CvType.CV_8UC3),borderWidth);
            }else{
                binds[1][i]=addRightBorder(matchedTiles.get(j).getImg(),borderWidth);
                j++;
            }
        }

        Mat result=stackImages(binds);
        HighGui.imshow("Tiles binds", rescaleImg(result,width));
        HighGui.waitKey();

    }

    private Mat stackImages(Mat[][] images) {
        if (images.length == 0 || images[0].length == 0) {
            throw new IllegalArgumentException("Input array must not be empty.");
        }

        // Vérifier que toutes les images ont la même taille
        int rows = images.length;
        int cols = images[0].length;
        int imgRows = images[0][0].rows();
        int imgCols = images[0][0].cols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (images[i][j].rows() != imgRows || images[i][j].cols() != imgCols) {
                    throw new IllegalArgumentException("All images must have the same size.");
                }
            }
        }

        // Créer une liste pour stocker les images concaténées
        List<Mat> concatenatedImages = new ArrayList<>();

        // Concaténer les images horizontalement
        for (int i = 0; i < rows; i++) {
            Mat rowConcatenated = new Mat();
            Core.hconcat(new ArrayList<>(Arrays.asList(images[i])), rowConcatenated);
            concatenatedImages.add(rowConcatenated);
        }

        // Concaténer les images verticalement
        Mat stackedImage = new Mat();
        Core.vconcat(concatenatedImages, stackedImage);

        return stackedImage;
    }


    private Mat addRightBorder(Mat img, int borderWidth){

        Mat blackBorder = Mat.zeros(img.rows(), borderWidth, CvType.CV_8UC3);
        Mat extendedImage = new Mat();
        Core.hconcat(List.of(new Mat[]{img, blackBorder}), extendedImage);

        return extendedImage;
    }



    public List<List<ImageTile>> findCluster(){
        List<List<ImageTile>> clusters=new ArrayList<>();
        for(int i=0;i<matchedTiles.size();i++){
            clusters.add(new ArrayList<>());
            clusters.get(i).add(matchedTiles.get(i));
        }
        int i1=0;
        int i2=0;
        double dist=-1;
        while(clusters.size()>5){
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

    public List<List<ImageTile>> concatList(List<List<ImageTile>> liste, int i1, int i2){
        for(ImageTile t:liste.get(i2)){
            liste.get(i1).add(t);
        }
        liste.remove(i2);
        return liste;
    }
    private double dMin(List<ImageTile> l1, List<ImageTile>l2){
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

        uploadedImg=rescaleImg(uploadedImg,targetX);
    }


    /**
     * Rescale an image to the given width, without changing the aspect ratio
     * @param newWidth the width of the new image
     * @return the resized image
     */
    private Mat rescaleImg(Mat img, int newWidth){
        Mat resized = new Mat();
        float scale=(float)newWidth/img.width();
        Size scaleSize=new Size(newWidth, img.height()*scale);
        Imgproc.resize(img,resized,scaleSize);
        return resized;
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
