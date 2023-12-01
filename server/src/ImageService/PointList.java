package ImageService;
import org.opencv.core.Point;

import java.util.ArrayList;

public class PointList {
    ArrayList<Point> points;
    public PointList(){
        this.points = new ArrayList<>();
    }
    public PointList(Point[] points){
        this.points = new ArrayList<>();

        for(Point point : points){
            this.insert(point);
        }
    }
    public void insert(Point point){
        this.points.add(point);
    }
    public void remove(Point point){
        this.points.remove(point);
    }

    public Point[] toArray(){
        Point[] points = new Point[this.points.size()];

        for(int i = 0; i < this.points.size(); ++i){
            points[i] = this.points.get(i);
        }

        return points;
    }
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
    private void invertPoints(int index1, int index2){
        Point p = this.points.get(index1);

        this.points.set(index1, this.points.get(index2));
        this.points.set(index2, p);
    }
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
    private double getDistanceBetween(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    @Override
    public String toString() {
        return "PointList: " + points;
    }
}
