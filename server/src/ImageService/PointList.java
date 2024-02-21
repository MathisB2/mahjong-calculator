package ImageService;
import org.opencv.core.Point;

import java.util.ArrayList;

/**
 * Class to manage Points
 */
public class PointList {
    ArrayList<Point> points;

    /**
     * initializes the ArrayList of Point points
     */
    public PointList(){
        this.points = new ArrayList<>();
    }

    /**
     * Constructor
     * @param points Array of Point objects to insert into points
     */
    public PointList(Point[] points){
        this.points = new ArrayList<>();

        for(Point point : points){
            this.insert(point);
        }
    }

    /**
     * Function which insert the Point passed as a parameter in points
     * @param point The Points to insert
     */
    public void insert(Point point){
        this.points.add(point);
    }

    /**
     * Function which delete the Point passed as parameter in points
     * @param point The Point to delete
     */
    public void remove(Point point){
        this.points.remove(point);
    }

    /**
     * Function that returns an array of Point objects from the ArrayList points
     * @return The array of Point objects
     */
    public Point[] toArray(){
        Point[] points = new Point[this.points.size()];

        for(int i = 0; i < this.points.size(); ++i){
            points[i] = this.points.get(i);
        }

        return points;
    }

    /**
     * Function that returns a PointList containing 4 Point objects, where each point is closest to the previous one and the first closest to (0;0)
     * @return The PointList with the 4 Point objects
     */
    public PointList toSortedList(){
        PointList dupList = new PointList(this.toArray());
        Point pointA = dupList.getClosestPointTo(new Point(0, 0));

        dupList.remove(pointA);
        Point pointB = dupList.getClosestPointTo(pointA);

        dupList.remove(pointB);
        Point pointC = dupList.getClosestPointTo(pointB);

        dupList.remove(pointC);
        Point pointD = dupList.getClosestPointTo(pointC);

        PointList result = new PointList(new Point[]{pointA, pointB, pointC, pointD});

        if(!result.isClockwise()){
            result.invertPoints(0, 1);
            result.invertPoints(2, 3);
        }
        return result;
    }

    /**
     * Function that checks that the Point objects in the ArrayList points are sorted clockwise
     * @return true if the Points are sorted correctly
     */
    private boolean isClockwise() {
        int size = this.points.size();
        if (size < 3) return false;

        double sum = 0;
        for (int i = 0; i < size; i++) {
            Point currentPoint = this.points.get(i);
            Point nextPoint = this.points.get((i + 1) % size);
            sum += (nextPoint.x - currentPoint.x) * (nextPoint.y + currentPoint.y);
        }
        return sum < 0;
    }

    /**
     * Function that reverses the position of two Point objects in the ArrayList points
     * @param index1 The first Point to reverse
     * @param index2 The Second Point to reverse
     */
    private void invertPoints(int index1, int index2){
        Point p = this.points.get(index1);

        this.points.set(index1, this.points.get(index2));
        this.points.set(index2, p);
    }

    /**
     * Function that returns the Point at the center of the ArrayList points
     * @return The Point in the center
     */
    public Point getCenter(){
        int numPoints = this.points.size();
        assert numPoints > 0 : "can't get center of empty list";

        double sumX = 0;
        double sumY = 0;

        for (Point point : this.points) {
            sumX += point.x;
            sumY += point.y;
        }

        return new Point(sumX / numPoints, sumY / numPoints);
    }

    /**
     * Function that returns the Point in points which is closest to the Point passed as parameter
     * @param point Point to test
     * @return The closest Point in points
     */
    public Point getClosestPointTo(Point point){
        Point closest = this.points.get(0);
        double minDistance = this.getDistanceBetween(point, closest);

        for (Point storedPoint : points) {
            double distance = this.getDistanceBetween(point, storedPoint);
            if (distance < minDistance) {
                minDistance = distance;
                closest = storedPoint;
            }
        }
        return closest;
    }

    /**
     * Function that returns the distance between two Point
     * @param p1 The fisrt Point
     * @param p2 the second Point
     * @return the distance between p1 and p2
     */
    private double getDistanceBetween(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    @Override
    public String toString() {
        return "PointList: " + points;
    }
}
