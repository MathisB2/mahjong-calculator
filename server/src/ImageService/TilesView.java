package ImageService;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TilesView {
    Size tileDimension;
    public TilesView(){
        this.tileDimension = new Size(280, 370);
    }

    public void showMatches(ArrayList<ImageTile> extractedTiles, ArrayList<ImageTile> matchedTiles){
        if(extractedTiles.size()==0 || matchedTiles.size()==0) return;
        int s = extractedTiles.size();
        Mat[][] binds = new Mat[2][s];
        Mat tmp;

        int borderWidth = 10;
        int j = 0;
        for (int i = 0; i < s; i++) {
            tmp = extractedTiles.get(i).getImg();
            Imgproc.putText(tmp, extractedTiles.get(i).getName(), new Point(10,40), Core.SORT_DESCENDING, 1.2, new Scalar(0, 0, 255), 2);
            binds[0][i] = addRightBorder(tmp,borderWidth);
            if(extractedTiles.get(i).getName().equals("")){
                binds[1][i] = addRightBorder(Mat.zeros(tileDimension, CvType.CV_8UC3),borderWidth);
            }else{
                binds[1][i] = addRightBorder(matchedTiles.get(j).getImg(),borderWidth);
                ++j;
            }
        }
        int width = 1600;
        this.showImage(rescaleImg(stackImages(binds), width));
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
