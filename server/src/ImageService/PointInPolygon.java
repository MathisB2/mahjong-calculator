package ImageService;

import org.opencv.core.Point;

/**
 * class which test if a point is in a polygon
 */
public class PointInPolygon {
    /**
     * Constructor of the class
     * test if a point is in a polygon
     * @param polygon Polygon Point table
     * @param point Point to test
     * @return a boolean which expresses the result
     */
    public static boolean pointInPolygon(Point[] polygon, Point point) {
        boolean odd = false;
        for (int i = 0, j = polygon.length - 1; i < polygon.length; i++) {
            if (((polygon[i].x > point.x) != (polygon[j].x > point.x))
                    && (point.y < (polygon[j].y - polygon[i].y) * (point.x - polygon[i].x) / (polygon[j].x - polygon[i].x) + polygon[i].y)) {

                odd = !odd;
            }
            j = i;
        }
        return odd;
    }
}