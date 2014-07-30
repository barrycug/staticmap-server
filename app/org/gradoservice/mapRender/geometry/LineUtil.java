package org.gradoservice.mapRender.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.02.14
 * Time: 12:07
 */
public class LineUtil {
    public static List<Point> simplify(List<Point> points, Double tolerance) {
        if (tolerance==null || points.isEmpty()) {
            return points.subList(0,points.size());
        }

        double sqTolerance = tolerance * tolerance;

        // stage 1: vertex reduction
        points = reducePoints(points, sqTolerance);

        // stage 2: Douglas-Peucker simplification
        points = simplifyDP(points, sqTolerance);

        return points;
    }

    public static double pointToSegmentDistance(Point p, Point p1, Point p2) {
        return Math.sqrt(sqClosestPointOnSegmentDistance(p, p1, p2));
    }

    private static List<Point> simplifyDP(List<Point> points, double sqTolerance) {

        int len = points.size();
        Integer[] markers = new Integer[len];

        markers[0] = markers[len - 1] = 1;

        simplifyDPStep(points, markers, sqTolerance, 0, len - 1);

         List<Point> newPoints = new ArrayList<Point>();

        for (int i = 0; i < len; i++) {
            if (markers[i]!=null) {
                newPoints.add(points.get(i));
            }
        }

        return newPoints;
    }

    private static void simplifyDPStep(List<Point> points, Integer[] markers, double sqTolerance, int first, int last) {

        double maxSqDist = 0;
        double sqDist = 0;
        int index = 0;

        for (int i = first + 1; i <= last - 1; i++) {
            sqDist = sqClosestPointOnSegmentDistance(points.get(i), points.get(first), points.get(last));

            if (sqDist > maxSqDist) {
                index = i;
                maxSqDist = sqDist;
            }
        }

        if (maxSqDist > sqTolerance) {
            markers[index] = 1;

            simplifyDPStep(points, markers, sqTolerance, first, index);
            simplifyDPStep(points, markers, sqTolerance, index, last);
        }
    }

    private static List<Point> reducePoints(List<Point> points, double sqTolerance) {
        List<Point> reducedPoints = new ArrayList<Point>();
        reducedPoints.add(points.get(0));
        int prev = 0;
        int len = points.size();

        for (int i = 1; i < len; i++) {
            if (sqDist(points.get(i), points.get(prev)) > sqTolerance) {
                reducedPoints.add(points.get(i));
                prev = i;
            }
        }

        if (prev < len - 1) {
            reducedPoints.add(points.get(len - 1));
        }
        return reducedPoints;
    }

    private static int _lastCode;

    public static Point[] clipSegment(Point a,Point b,Bounds bounds,boolean useLastCode) {
        int codeA = useLastCode ? _lastCode : getBitCode(a, bounds);
        int codeB = getBitCode(b, bounds);

        int codeOut;
        int newCode;
        Point p;

        // save 2nd code to avoid calculating it on the next segment
        _lastCode = codeB;

        while (true) {
            // if a,b is inside the clip window (trivial accept)
            if ((codeA | codeB)==0) {
                return new Point[]{a, b};
                // if a,b is outside the clip window (trivial reject)
            } else if ((codeA & codeB)!=0) {
                return null;
                // other cases
            } else {
                codeOut = codeA!=0 ? codeA : codeB;
                p = getEdgeIntersection(a, b, codeOut, bounds);
                newCode = getBitCode(p, bounds);

                if (codeOut == codeA) {
                    a = p;
                    codeA = newCode;
                } else {
                    b = p;
                    codeB = newCode;
                }
            }
        }
    }

    protected static Point getEdgeIntersection(Point a,Point b,int code,Bounds bounds) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        Point  min = bounds.getMin();
        Point  max = bounds.getMax();
        double x = 0;
        double y = 0;

        if ((code & 8)!=0) { // top
            x = a.getX() + dx * (max.getY() - a.getY()) / dy;
            y = max.getY();

        } else if ((code & 4)!=0) { // bottom
            x = a.getX() + dx * (min.getY() - a.getY()) / dy;
            y = min.getY();

        } else if ((code & 2)!=0) { // right
            x = max.getX();
            y = a.getY() + dy * (max.getX() - a.getX()) / dx;

        } else if ((code & 1)!=0) { // left
            x = min.getX();
            y = a.getY() + dy * (min.getX() - a.getX()) / dx;
        }

        return new Point(x, y, true);
    }

    protected static int getBitCode(Point p,Bounds bounds) {
        int code = 0;

        if (p.getX() < bounds.getMin().getX()) { // left
            code |= 1;
        } else if (p.getX() > bounds.getMax().getX()) { // right
            code |= 2;
        }

        if (p.getY() < bounds.getMin().getY()) { // bottom
            code |= 4;
        } else if (p.getY() > bounds.getMax().getY()) { // top
            code |= 8;
        }

        return code;
    }


    private static double sqDist(Point p1,Point  p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return dx * dx + dy * dy;
    }

    private static double sqClosestPointOnSegmentDistance(Point p, Point p1, Point p2) {
        double x = p1.getX();
        double y = p1.getY();
        double dx = p2.getX()-x;
        double dy = p2.getY()-y;
        double dot = dx * dx + dy * dy;
        double t = 0;

        if (dot > 0) {
            t = ((p.getX() - x) * dx + (p.getY() - y) * dy) / dot;

            if (t > 1) {
                x = p2.getX();
                y = p2.getY();
            } else if (t > 0) {
                x += dx * t;
                y += dy * t;
            }
        }

        dx = p.getX() - x;
        dy = p.getY() - y;

        return dx * dx + dy * dy;
    }

    private static Point sqClosestPointOnSegment(Point p, Point p1, Point p2) {
        double x = p1.getX();
        double y = p1.getY();
        double dx = p2.getX()-x;
        double dy = p2.getY()-y;
        double dot = dx * dx + dy * dy;
        double t = 0;

        if (dot > 0) {
            t = ((p.getX() - x) * dx + (p.getY() - y) * dy) / dot;

            if (t > 1) {
                x = p2.getX();
                y = p2.getY();
            } else if (t > 0) {
                x += dx * t;
                y += dy * t;
            }
        }

        dx = p.getX() - x;
        dy = p.getY() - y;

        return new Point(x,y);
    }

    public static Point closestPointOnSegment(Point p, Point p1, Point p2) {
        return sqClosestPointOnSegment(p, p1, p2);
    }




}
