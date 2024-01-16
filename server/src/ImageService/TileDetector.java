package ImageService;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileDetector {
    Size tileDimension;
    DataSet dataset;
    int resolution;
    public TileDetector(DataSet dataSet, int resolution){
        this.dataset = dataSet;
        this.tileDimension = new Size(280, 370);
        this.resolution = resolution;

        dataset.sizeTo(this.tileDimension);
    }
    public ArrayList<ImageTile> extractTiles(String imagePath){
        return extractTiles(Imgcodecs.imread(imagePath));
    }
    public ArrayList<ImageTile> extractTiles(Mat image){
        this.rescaleImgTo(image, this.resolution);
        ArrayList<ImageTile> extractedTiles = new ArrayList<>();
        ArrayList<MatOfPoint> contours = getContours(image);

        for (MatOfPoint cnt : contours) {
            PointList inputPts = new PointList(cnt.toArray()).toSortedList();
            MatOfPoint2f src = new MatOfPoint2f(inputPts.toArray());
            MatOfPoint2f dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(tileDimension.width, 0), new Point(tileDimension.width, tileDimension.height), new Point(0, tileDimension.height)});

            Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);
            Mat finalImage = new Mat();
            Imgproc.warpPerspective(image, finalImage, perspectiveTransform, tileDimension);

            ImageTile t = new ImageTile("", finalImage);

            Point center = inputPts.getCenter();
            t.x = (int) center.x;
            t.y = (int) center.y;

            extractedTiles.add(t);
        }
        return extractedTiles;
    }

    public ArrayList<ImageTile> getMatchedTilesTo(ArrayList<ImageTile> extractedTiles){
        ArrayList<Thread> ths = new ArrayList<>();
        ArrayList<ImageTile> matchedTiles = new ArrayList(extractedTiles.size());

        for(int i=0;i<extractedTiles.size();i++){
            ImageTile t = extractedTiles.get(i);

            Thread th = new Thread(() ->  {
                ImageTile result = this.dataset.findMatchingTile(t.getImg());
                t.setName(result.getName());
                result.x = t.x;
                result.y = t.y;

                extractedTiles.add(t);

                if(!t.getName().equals("")){
                    matchedTiles.add(result);
                }
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

        return matchedTiles;
    }
    private void rescaleImgTo(Mat img, int newWidth){
        if(img.width() < img.height()){
            Core.rotate(img, img, Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        float scale = (float)newWidth / img.width();
        Size scaleSize = new Size(newWidth, img.height() * scale);
        Imgproc.resize(img, img, scaleSize);
    }

    private ArrayList<MatOfPoint> getContours(Mat image){
        ArrayList<MatOfPoint> contours = new ArrayList();
        ArrayList<MatOfPoint> tmp = getTmpFrom(image);

        for(MatOfPoint cnt : tmp){
            if (Imgproc.contourArea(cnt) < 1000) continue;

            MatOfPoint quad = simplifyContour(cnt, 4);
            if(quad.toArray().length != 4) continue;

            contours.add(quad);
        }

        return contours;
    }

    private ArrayList<MatOfPoint> getTmpFrom(Mat image){
        Mat clone = new Mat();
        int dilateSize = 10;

        Imgproc.cvtColor(image, clone, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(clone,clone,new Size(5,5));
        Imgproc.threshold(clone, clone, 0, 1, Imgproc.THRESH_OTSU);

        Mat tilesMask = new Mat();
        Imgproc.dilate(clone, tilesMask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateSize, dilateSize)));

        Mat maskBGR = new Mat();
        List<Mat> listMat = Arrays.asList(tilesMask, tilesMask, tilesMask);
        Core.merge(listMat, maskBGR);

        Core.multiply(image, maskBGR, clone);
        Imgproc.cvtColor(clone, clone, Imgproc.COLOR_BGR2GRAY);

        Imgproc.adaptiveThreshold(clone, clone, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 7, 4);

        dilateSize = 4;
        Imgproc.erode(clone, clone, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateSize, dilateSize)));

        Core.multiply(clone, tilesMask, clone);


        ArrayList<MatOfPoint> tmp = new ArrayList<>();
        Imgproc.findContours(clone, tmp, clone, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        return tmp;
    }

    private MatOfPoint simplifyContour(MatOfPoint cnt, int nCorners) {
        double lb = 0.0;
        double ub = 1.0;

        for(int i = 0; i < 100; ++i) {
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
}
