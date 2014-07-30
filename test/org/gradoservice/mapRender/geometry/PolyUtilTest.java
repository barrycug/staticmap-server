package org.gradoservice.mapRender.geometry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class PolyUtilTest {
    @Test
    public void clipPolygonCheck() throws Exception {
        Bounds bounds = new Bounds(new Point(0, 0), new Point(10, 10));

        Point[] values = {
                new Point(5, 5),
                new Point(15, 10),
                new Point(10, 15)
        };

        List<Point> points =  new ArrayList(Arrays.asList(values));

        List<Point> clipped = PolyUtil.clipPolygon(points, bounds);

        assertThat(clipped).containsExactly(
                new Point(8, 10),
                new Point(5, 5),
                new Point(10, 8),
                new Point(10, 10)
        );
    }
}
