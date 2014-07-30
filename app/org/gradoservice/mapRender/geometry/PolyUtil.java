package org.gradoservice.mapRender.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.02.14
 * Time: 13:29
 */
public class PolyUtil extends LineUtil {
    public static List<Point> clipPolygon(List<Point> points, Bounds bounds) {
        List<Point> clippedPoints;

        int[] edges = {1, 4, 2, 8};
        /*
        i, j, k,
                a, b,
                len, edge, p,
                lu = L.LineUtil;   */

        final int len = points.size();

        for (Point point : points) {
            point.setCode(LineUtil.getBitCode(point, bounds));
        }

        // for each edge (left, bottom, right, top)
        for (int k = 0; k < 4; k++) {
            int edge = edges[k];
            clippedPoints = new ArrayList<Point>();

            int j = len - 1;

            int pointsLen = points.size();

            for (int i = 0; i < pointsLen; j = i++) {

                Point a = points.get(i);
                Point b = points.get(j);
                int acode = a.getCode();
                int bcode = b.getCode();

                // if a is inside the clip window
                if ((acode & edge)==0) {
                    // if b is outside the clip window (a->b goes out of screen)
                    if ((bcode & edge)!=0) {
                        Point p = getEdgeIntersection(b, a, edge, bounds);
                        p.setCode(LineUtil.getBitCode(p, bounds));
                        clippedPoints.add(p);
                    }
                    clippedPoints.add(a);

                    // else if b is inside the clip window (a->b enters the screen)
                } else if ((bcode & edge)==0) {
                    Point p = getEdgeIntersection(b, a, edge, bounds);
                    p.setCode(LineUtil.getBitCode(p, bounds));
                    clippedPoints.add(p);
                }
            }
            points = clippedPoints;
        }

        return points;
    }
}
