package org.gradoservice.mapRender.geometry;

import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */
public class TransformationCheck {
    private Transformation t = new Transformation(1, 2, 3, 4);
    Point p = new Point(10, 20);

    @Test
    public void transform() {
        Point p2 = t.transform(p, 2.0);
        assertThat(p2).isEqualTo(new Point(24, 128));

        p2 = t.transform(p);
        assertThat(p2).isEqualTo(new Point(12, 64));
    }

    @Test
    public void untransform() {
        Point p2 = t.transform(p, 2.0);
        Point p3 = t.untransform(p2, 2.0);
        assertThat(p3).isEqualTo(p);

        p2 = t.transform(p);
        assertThat(t.untransform(new Point(12, 64))).isEqualTo(new Point(10, 20));
    }

}
