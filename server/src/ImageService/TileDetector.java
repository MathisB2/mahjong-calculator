package ImageService;

import ImageService.Tiles.DataSet;
import ImageService.Tiles.ExtractedTile;
import ImageService.Tiles.MatchedTile;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

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
    public ArrayList<MatchedTile> getMatchedTilesOn(Mat image){
        var extractedTiles = extractTiles(image);
        return getMatchedTilesTo(extractedTiles);
    }

    public ArrayList<MatchedTile> getMatchedTilesTo(ArrayList<ExtractedTile> extractedTiles){
        ArrayList<Thread> ths = new ArrayList<>();
        ArrayList<MatchedTile> matchedTiles = new ArrayList(extractedTiles.size());

        for(int i = 0; i < extractedTiles.size(); ++i){
            int index = i;
            ImageTile extractedTile = extractedTiles.get(index);

            Thread th = new Thread(() ->  {
                ImageTile result = this.dataset.findMatchingTile(extractedTile.getImg());
                if(result == null) return;

                matchedTiles.add(new MatchedTile(result, extractedTile));

            });
            ths.add(th);
            th.start();
        }

        try{
            for(Thread th : ths){
                th.join();
            }
        }catch(Exception e){
            System.out.print(e);
        }

        return matchedTiles;
    }

    public ArrayList<ExtractedTile> extractTiles(String imagePath){
        return extractTiles(Imgcodecs.imread(imagePath));
    }
    public ArrayList<ExtractedTile> extractTiles(Mat image){
        this.rescaleImgTo(image, this.resolution);
        ArrayList<ExtractedTile> extractedTiles = new ArrayList<>();

        ImageShapes imageShapes = new ImageShapes(image);
        imageShapes.keep(3000,100000);
        imageShapes.simplify(4);
        imageShapes.keepExternalShapes();
        imageShapes.draw();
        var shapes = imageShapes.getShapes();

        for (MatOfPoint cnt : shapes) {
            PointList inputPts = new PointList(cnt.toArray()).toSortedList();
            MatOfPoint2f src = new MatOfPoint2f(inputPts.toArray());
            MatOfPoint2f dst = new MatOfPoint2f(new Point[]{new Point(0, 0), new Point(tileDimension.width, 0), new Point(tileDimension.width, tileDimension.height), new Point(0, tileDimension.height)});

            Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);
            Mat finalImage = new Mat();
            Imgproc.warpPerspective(image, finalImage, perspectiveTransform, tileDimension);

            ExtractedTile t = new ExtractedTile(finalImage);

            Point center = inputPts.getCenter();
            t.x = (int) center.x;
            t.y = (int) center.y;

            extractedTiles.add(t);
        }
        return extractedTiles;
    }

    private void rescaleImgTo(Mat img, int newWidth){
        if(img.width() < img.height()){
            Core.rotate(img, img, Core.ROTATE_90_COUNTERCLOCKWISE);
        }

        float scale = (float)newWidth / img.width();
        Size scaleSize = new Size(newWidth, img.height() * scale);
        Imgproc.resize(img, img, scaleSize);
    }
}
