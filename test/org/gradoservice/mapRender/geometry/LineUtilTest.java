package org.gradoservice.mapRender.geometry;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 23:04
 * To change this template use File | Settings | File Templates.
 */
public class LineUtilTest {
    private Bounds bounds = new Bounds(new Point(5, 0), new Point(15, 10));

    @Test
    public void clipSegmentByBoundsCheck() throws Exception {
        Point a = new Point(0, 0);
        Point b = new Point(15, 15);

        Point[] segment = LineUtil.clipSegment(a, b, bounds, false);

        assertThat(segment[0]).isEqualTo(new Point(5, 5));
        assertThat(segment[1]).isEqualTo(new Point(10, 10));

        Point c = new Point(5, -5);
        Point d = new Point(20, 10);

        Point[] segment2 = LineUtil.clipSegment(c, d, bounds, false);

        assertThat(segment2[0]).isEqualTo(new Point(10, 0));
        assertThat(segment2[1]).isEqualTo(new Point(15, 5));
    }

    @Test
    public void clipSegmentOutOfBoundsCheck() throws Exception {
        Point a = new Point(15, 15);
        Point b = new Point(25, 20);
        Point[] segment = LineUtil.clipSegment(a, b, bounds, false);

        assertThat(segment).isNullOrEmpty();
    }

    @Test
    public void pointToSegmentDistanceCheck() throws Exception {
        Point p1 = new Point(0, 10);
        Point p2 = new Point(10, 0);
        Point p = new Point(0, 0);

        assertThat(LineUtil.pointToSegmentDistance(p, p1, p2)).isEqualTo(Math.sqrt(200) / 2);

        assertThat(LineUtil.closestPointOnSegment(p, p1, p2)).isEqualTo(new Point(5, 5));
    }

    @Test
    public void simplifyTest() throws Exception {
        Point[] values = {
                new Point(0, 0),
                new Point(0.01, 0),
                new Point(0.5, 0.01),
                new Point(0.7, 0),
                new Point(1, 0),
                new Point(1.999, 0.999),
                new Point(2, 1)
        };

        List<Point> points =  new ArrayList(Arrays.asList(values));

        List<Point> simplified = LineUtil.simplify(points, 0.1);

        assertThat(simplified).containsExactly(
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 1)
        );
    }
}
