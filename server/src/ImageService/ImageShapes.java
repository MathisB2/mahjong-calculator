package ImageService;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageShapes {
    private ArrayList<MatOfPoint> shapes;
    private Mat image;

    ImageShapes(Mat image){
        this.image = image;
        this.shapes = new ArrayList();

        detectShapes();
    }

    private void detectShapes(){
        assert this.shapes.size() == 0 : "shapes are already detected";
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

        Imgproc.adaptiveThreshold(clone, clone, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 13, 4);
        (new TilesView()).showImage(clone);

        dilateSize = 6;
        Imgproc.erode(clone, clone, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilateSize, dilateSize)));
        (new TilesView()).showImage(clone);

        Core.multiply(clone, tilesMask, clone);
        Imgproc.findContours(clone, shapes, clone, Imgproc.CHAIN_APPROX_NONE, Imgproc.CHAIN_APPROX_SIMPLE);
    }

    public ArrayList<MatOfPoint> getShapes(){ return this.shapes; }
    public void keep(int minArea, int maxArea) {
        for (int i = 0; i < shapes.size();) {
            MatOfPoint shape = shapes.get(i);
            double area = Imgproc.contourArea(shape);

            if (area < minArea || area > maxArea) shapes.remove(i);
            else ++i;
        }
    }
    public void keepExternalShapes(){
        for (int i = 0; i < shapes.size();) {
            var shape1 = shapes.get(i);

            if(isExternalShape(shape1))
                ++i;
            else
                shapes.remove(i);
        }
    }

    private boolean isExternalShape(MatOfPoint shape1){
        double area1 = Imgproc.contourArea(shape1);
        Point[] points1 = shape1.toArray();

        for(var shape2 : shapes){
            if(area1 >= Imgproc.contourArea(shape2)) continue;
            Point[] points2 = shape2.toArray();

            int nbrInternalPoints = 0;
            for(var point : points1){
                if(PointInPolygon.pointInPolygon(points2, point)) ++nbrInternalPoints;
                if(nbrInternalPoints >= 3) return false;
            }
        }

        return true;
    }

    public void simplify(int nCorners){
        ArrayList<MatOfPoint> simplifiedShapes = new ArrayList<>();
        for (var shape : shapes) {
            var simplified = simplifyShape(shape, nCorners);

            if (simplified.toArray().length != nCorners) continue;
            simplifiedShapes.add(simplified);
        }

        this.shapes = simplifiedShapes;
    }

    private MatOfPoint simplifyShape(MatOfPoint shape, int nCorners){
        double lb = 0.0;
        double ub = 1.0;
        double k;
        double eps;

        MatOfPoint2f approx = new MatOfPoint2f();
        MatOfPoint2f shape2f = new MatOfPoint2f(shape.toArray());

        for(int i = 0; i < 100; ++i) {
            k = (lb + ub) / 2.0;
            eps = k * Imgproc.arcLength(new MatOfPoint2f(shape.toArray()), true);

            Imgproc.approxPolyDP(shape2f, approx, eps, true);

            if (approx.total() > nCorners) {
                lb = (lb + ub) / 2.0;
            } else if (approx.total() < nCorners) {
                ub = (lb + ub) / 2.0;
            } else {
                return new MatOfPoint(approx.toArray());
            }
        }
        return shape;
    }
    public void draw(){
        Scalar color = new Scalar(255, 0, 0);
        var imgCloned = image.clone();
        for(int i = 0; i < this.shapes.size(); ++i){
            Imgproc.drawContours(imgCloned, shapes, i, color);
        }
        (new TilesView()).showImage(imgCloned);
    }
}
