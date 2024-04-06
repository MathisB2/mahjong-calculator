package ImageService;

import Clustering.Cluster;
import Clustering.ClusterPoint;
import Clustering.Clusters;
import ImageService.Tiles.ImageTile;
import ImageService.Tiles.MatchedTile;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TilesView {
    Size tileDimension;
    public TilesView(){
        this.tileDimension = new Size(280, 370);
    }

    public void showMatches(ArrayList<MatchedTile> matchedTiles){
        if(matchedTiles.size()==0) return;
        int s = matchedTiles.size();
        Mat[][] binds = new Mat[2][s];
        Mat tmp;

        int borderWidth = 10;
        int j = 0;
        for (int i = 0; i < s; i++) {
            tmp = matchedTiles.get(i).getExtractedImg().clone();
            Imgproc.putText(tmp, matchedTiles.get(i).getName(), new Point(10,40), Core.SORT_DESCENDING, 1.2, new Scalar(0, 0, 255), 2);
            binds[0][i] = addRightBorder(tmp,borderWidth);

            if(matchedTiles.get(i).getName().equals("")){
                binds[1][i] = addRightBorder(Mat.zeros(tileDimension, CvType.CV_8UC3),borderWidth);
            }else{
                binds[1][i] = addRightBorder(matchedTiles.get(j).getImg(),borderWidth);
                ++j;
            }
        }
        int width = 1600;
        this.showImage(rescaleImg(stackImages(binds), width));
    }

    public void showClusters(Mat image, Clusters clusters){
        Mat imgClusters= image.clone();
        int count=0;
        for (Cluster c:clusters){
            count++;
            for(ClusterPoint p:c){
                Imgproc.putText(imgClusters, String.valueOf(count), new Point(p.x,p.y), Core.SORT_DESCENDING, 2, new Scalar(255, 255, 255), 8);
                Imgproc.putText(imgClusters, String.valueOf(count), new Point(p.x,p.y), Core.SORT_DESCENDING, 2, new Scalar(0, 0, 255), 4);
            }
        }
        this.showImage(imgClusters);
    }


    public void showImage(Mat imageMat) {
        JFrame jf = new JFrame();

        BufferedImage img = (BufferedImage) HighGui.toBufferedImage(imageMat);

        jf.getContentPane().add(new JLabel(new ImageIcon(img)));
        jf.setSize(new Dimension(img.getWidth(), img.getHeight()+20));
        jf.setVisible(true);
    }

    private Mat addRightBorder(Mat img, int borderWidth){
        Mat blackBorder = Mat.zeros(img.rows(), borderWidth, CvType.CV_8UC3);
        Mat extendedImage = new Mat();
        Core.hconcat(List.of(new Mat[]{img, blackBorder}), extendedImage);

        return extendedImage;
    }
    private Mat stackImages(Mat[][] images) {
        if (images.length == 0 || images[0].length == 0)
            throw new IllegalArgumentException("Input array must not be empty.");

        int imgRows = images[0][0].rows();
        int imgCols = images[0][0].cols();

        for (int i = 0; i < images.length; i++) {
            for (int j = 0; j < images[0].length; j++) {
                var image = images[i][j];

                if (image.rows() != imgRows || image.cols() != imgCols) {
                    images[i][j] = ImageTile.NULL.getImg();
                }
            }
        }

        List<Mat> concatenatedImages = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            Mat rowConcatenated = new Mat();
            Core.hconcat(new ArrayList<>(Arrays.asList(images[i])), rowConcatenated);
            concatenatedImages.add(rowConcatenated);
        }
        Mat stackedImage = new Mat();
        Core.vconcat(concatenatedImages, stackedImage);

        return stackedImage;
    }
    private Mat rescaleImg(Mat img, int newWidth){
        Mat resized = new Mat();
        float scale = (float)newWidth / img.width();
        Size scaleSize = new Size(newWidth, img.height() * scale);
        Imgproc.resize(img,resized,scaleSize);
        return resized;
    }
}
