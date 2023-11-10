
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class AddingImagesTrackbar {
    private static final int ALPHA_SLIDER_MAX = 255;
    private int sliderValue1 = 5;
    private int sliderValue2 = 100;
    private int suggest;
    private Mat matImgSrc1;
    private Mat matImgDst = new Mat();
    private JFrame frame;
    private JLabel imgLabel;

    public AddingImagesTrackbar(String[] args) {
        String imagePath1 = "src/img/tg.jpg";

        matImgSrc1 = Imgcodecs.imread(imagePath1);
        if (matImgSrc1.empty()) {
            System.out.println("Empty image: " + imagePath1);
            System.exit(0);
        }

        if(matImgSrc1.width()<matImgSrc1.height()){
            Core.rotate(matImgSrc1,matImgSrc1,Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        //rescale image
        int targetX=1600;
        float scale=(float)targetX/matImgSrc1.width();
        Size scaleSize=new Size(targetX, matImgSrc1.height()*scale);
        Imgproc.resize(matImgSrc1,matImgSrc1,scaleSize);

        Imgproc.cvtColor(matImgSrc1, matImgSrc1, Imgproc.COLOR_BGR2GRAY);

        sliderValue2=suggest=getMostDominantColor(matImgSrc1);


        // Create and set up the window.
        frame = new JFrame("Canny edge detection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set up the content pane.
        Image img = HighGui.toBufferedImage(matImgSrc1);
        addComponentsToPane(frame.getContentPane(), img);
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        frame.pack();
        frame.setVisible(true);
        update();
    }

    private void addComponentsToPane(Container pane, Image img) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel(String.format("value 1: x %d", ALPHA_SLIDER_MAX)));
        JSlider slider1 = new JSlider(0, ALPHA_SLIDER_MAX, sliderValue1);
        slider1.setMajorTickSpacing(20);
        slider1.setMinorTickSpacing(5);
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                AddingImagesTrackbar.this.sliderValue1 = source.getValue();
                update();
            }
        });
        sliderPanel.add(slider1);


        JSlider slider2 = new JSlider(0, ALPHA_SLIDER_MAX, sliderValue2);
        slider2.setMajorTickSpacing(20);
        slider2.setMinorTickSpacing(5);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                AddingImagesTrackbar.this.sliderValue2 = source.getValue();
                update();
            }
        });
        sliderPanel.add(slider2);
        pane.add(sliderPanel, BorderLayout.PAGE_START);
        imgLabel = new JLabel(new ImageIcon(img));
        pane.add(imgLabel, BorderLayout.CENTER);
    }

    private void update() {

        System.out.println("Blur : "+sliderValue1);
        System.out.println("Tresh : "+sliderValue2);
        System.out.println("Suggested tresh : "+suggest);

        Imgproc.blur(matImgSrc1,matImgDst,new Size(sliderValue1,sliderValue1));
        Imgproc.threshold(matImgDst,matImgDst,sliderValue2,255,Imgproc.THRESH_BINARY);
        /*
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(matImgDst, contours, matImgDst, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint cnt : contours) {
            Imgproc.drawContours(matImgDst, contours, contours.indexOf(cnt), new Scalar(0, 255, 255), 3);
        }*/


        //Imgproc.Canny(matImgSrc1,matImgDst, sliderValue1,sliderValue2);

        Image img = HighGui.toBufferedImage(matImgDst);
        imgLabel.setIcon(new ImageIcon(img));
        frame.repaint();
    }


    public int getMostDominantColor(Mat img) {
        //image must be gray
        double moy = 0;
        int count = 0;

        for (int y = 0; y < img.rows(); y++) {
            for (int x = 0; x < img.cols(); x++) {
                moy += matImgSrc1.get(y, x)[0];
                count++;
            }
        }
        return (int)(moy/count)+30;
    }


    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddingImagesTrackbar(args);
            }
        });
    }
}

