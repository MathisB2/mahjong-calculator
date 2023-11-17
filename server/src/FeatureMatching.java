import org.opencv.core.*;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.FlannBasedMatcher;
import org.opencv.features2d.SIFT;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class FeatureMatching {
    public static void main(String[] args) {

        List<Integer> ttt=new ArrayList<>();
        for(int i=0;i<13;i++){
            ttt.add(i);
        }
        for(int i=0;i<ttt.size();i++){
            for(int y=i+1;y<ttt.size();y++){
                System.out.println(i+" : "+y);
            }
        }
    }
}
