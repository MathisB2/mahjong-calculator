import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenCVTest {
    public static int getMainColor(Mat img) {
        return getMainColor(img,false);
    }
    public static int getMainColor(Mat img, boolean ignoreZeros) {
        if (img.channels()!=1) {
            throw new IllegalArgumentException("input image must have only one chanel");
        }
        //image must be gray
        double moy = 0;
        int count = 0;

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                if(!ignoreZeros || img.get(y,x)[0]!=0){
                    moy += img.get(y, x)[0];
                    count++;
                }

            }
        }
        return (int)(moy/count);
    }


    public static int getRecomendedTreshold(Mat img){
        return getRecomendedTreshold(img,false);
    }
    public static int getRecomendedTreshold(Mat img,boolean ingoreZeros){
        return getMainColor(img,ingoreZeros)+20;
    }


    public static double mapRange(double value, double fromMin, double fromMax, double toMin, double toMax) {
        if (fromMin == fromMax) {
            throw new IllegalArgumentException("fromMin and fromMax can't be the same value");
        }

        return  (value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin;
    }

    public static Mat normalize(Mat img) {
        double min=255;
        double max=0;

        //trouver le min et le max parmi tous les chanels
        //logiquement min=treshold
        // appeler mapRange(img.get(y,x),min,max,0,255)


        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                for (double chanel:img.get(y,x)) {
                    if(chanel<min){
                        min=chanel;
                    }
                    if(chanel>max){
                        max=chanel;
                    }
                }
            }
        }
        Mat normalized = img.clone();
        int ch=img.channels();
        double[] tmp=new double[ch];

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                for (int i=0;i<ch;i++) {
                   tmp[i]=mapRange(img.get(y,x)[i],min,max,0,255);
                }
                normalized.put(y,x,tmp);
            }
        }


        System.out.println("min : "+min);
        System.out.println("max : "+max);
        return normalized;
    }


    public static Mat replaceColorUnderTreshold(Mat img,int treshold) {

        Mat gray = img.clone();
        if(img.channels()!=1){
            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);
        }


        Mat replaced = img.clone();
        double[] tmp={treshold,treshold,treshold};

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                if(gray.get(y,x)[0]<=treshold){
                    replaced.put(y,x,tmp);
                }

            }
        }
        return replaced;
    }


    public static void main(String[] args) {

        int rescaleTarget=1600;
        int blurRadius=4;

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat original = Imgcodecs.imread("src/img/render6.png");

        if(original.width()<original.height()){
            Core.rotate(original,original,Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        //rescale image
        float scale=(float)rescaleTarget/original.width();
        Size scaleSize=new Size(rescaleTarget, original.height()*scale);
        Imgproc.resize(original,original,scaleSize);


        Mat img = original.clone();
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(img,img,new Size(blurRadius,blurRadius));

        System.out.println("Recomended treshold : "+getRecomendedTreshold(img));
//        HighGui.imshow("Base", img);
//        HighGui.waitKey();
//        HighGui.destroyAllWindows();


        System.out.println(img.channels());
        Mat replaced=replaceColorUnderTreshold(img,getRecomendedTreshold(img));


//        HighGui.imshow("Replaced", replaced);
//        HighGui.waitKey();
//        HighGui.destroyAllWindows();


        Mat normalized= normalize(replaced);
//        HighGui.imshow("Normalized", normalized);
//        HighGui.waitKey();
//        HighGui.destroyAllWindows();


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
    }



}
