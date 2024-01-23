package ImageService;

import org.opencv.core.Point;

public class PointInPolygon {
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