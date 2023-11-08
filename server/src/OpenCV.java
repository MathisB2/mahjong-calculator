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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class OpenCV {
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        // load image
        Mat img = Imgcodecs.imread("src/img/render6.png");

        if(img.width()<img.height()){
            Core.rotate(img,img,Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        //rescale image
        int targetX=1600;
        float scale=(float)targetX/img.width();
        Size scaleSize=new Size(targetX, img.height()*scale);
        Imgproc.resize(img,img,scaleSize);


        HighGui.imshow("Shapes", img);
        HighGui.waitKey();
        HighGui.destroyAllWindows();




        //convert to gray
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(gray,gray,new Size(5,5));

        //Imgproc.adaptiveThreshold(gray, gray, 255,1, 1, 11, 2);
        Imgproc.threshold(gray,gray,0,255,Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);
        //Imgproc.Canny(gray,gray,85,255,3);
        HighGui.imshow("Shapes", gray);
        HighGui.waitKey();
        HighGui.destroyAllWindows();
        // find contours

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(gray, contours, gray, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int nbContours=0;
        for (MatOfPoint cnt : contours) {
            //System.out.println("test");
            //Imgproc.drawContours(img, contours, contours.indexOf(cnt), new Scalar(0, 255, 255), 3);

            MatOfPoint2f m2p= new MatOfPoint2f();
            double epsilon=0.1*Imgproc.arcLength(new MatOfPoint2f(cnt.toArray()),true);
            Imgproc.approxPolyDP(new MatOfPoint2f(cnt.toArray()),m2p,epsilon,true);

            MatOfPoint approx=new MatOfPoint();
            m2p.convertTo(approx,CvType.CV_32S);

            Imgproc.drawContours(img, Arrays.asList(approx),0,new Scalar(0,0,255),2);

            ArrayList<Integer> four=new ArrayList<Integer>();

            int[] n=new int[(int)(approx.total()*approx.channels())];
            approx.get(0,0,n);
            for(int j=0;j<n.length;j+=2){
                int x = n[j];
                int y = n[j+1];
                String coordinates=Integer.toString(x)+" ; "+Integer.toString(y);
                Imgproc.putText(img,coordinates,new Point(x,y),Imgproc.FONT_HERSHEY_COMPLEX,0.25,new Scalar(255,0,0));
                four.add(x);
                four.add(y);
            }

            int width=250;
            int height=350;
            //Point[] inputPts=new Point[]{new Point(four.get(6),four.get(7)),new Point(four.get(0),four.get(1)),new Point(four.get(2),four.get(3)),new Point(four.get(4),four.get(5))};
            Point[] inputPts=new Point[]{new Point(four.get(0),four.get(1)),new Point(four.get(6),four.get(7)),new Point(four.get(4),four.get(5)),new Point(four.get(2),four.get(3))};
            Point[] outputPts=new Point[]{new Point(0,0),new Point(width,0),new Point(width,height),new Point(0,height)};
            MatOfPoint2f src=new MatOfPoint2f(inputPts);
            MatOfPoint2f dst=new MatOfPoint2f(outputPts);


            Mat perspectivetransform=Imgproc.getPerspectiveTransform(src,dst);
            Mat finalImage=new Mat();
            Imgproc.warpPerspective(img,finalImage,perspectivetransform,new Size(250,350));

            System.out.println(four);


            HighGui.imshow("Shapes", finalImage);
            HighGui.waitKey();
            HighGui.destroyAllWindows();

        }

        // show
        HighGui.imshow("Shapes", img);
        HighGui.waitKey();
        HighGui.destroyAllWindows();

    }
}
