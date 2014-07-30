package org.gradoservice.mapRender.geometry;

import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class BoundsTest {
    private Bounds a = new Bounds(
            new Point(14, 12),
            new Point(30, 40));
    private Bounds b = new Bounds(
            new Point(20, 12),
            new Point(14, 20)
            ).extend(new Point(30, 40));

    private Bounds c = new Bounds();

    @Test
    public void constructorCheck() throws Exception {
        assertThat(a.getMin()).isEqualTo(new Point(14, 12));
        assertThat(a.getMax()).isEqualTo(new Point(30, 40));

        assertThat(b.getMin()).isEqualTo(new Point(14, 12));
        assertThat(b.getMax()).isEqualTo(new Point(30, 40));
    }

    @Test
    public void extendCheck() throws Exception {
        a.extend(new Point(50, 20));
        assertThat(a.getMin()).isEqualTo(new Point(14, 12));
        assertThat(a.getMax()).isEqualTo(new Point(50, 40));

        b.extend(new Point(25, 50));
        assertThat(b.getMin()).isEqualTo(new Point(14, 12));
        assertThat(b.getMax()).isEqualTo(new Point(30, 50));
    }

    @Test
    public void getCenterCheck() throws Exception {
        assertThat(a.getCenter()).isEqualTo(new Point(22, 26));
    }

    @Test
    public void containsCheck() throws Exception {
        a.extend(new Point(50, 10));
        assertThat(a.contains(b)).isEqualTo(true);
        assertThat(b.contains(a)).isEqualTo(false);
        assertThat(a.contains(new Point(24, 25))).isTrue();
        assertThat(a.contains(new Point(54, 65))).isFalse();
    }

    @Test
    public void getSizeCheck() throws Exception {
        assertThat(a.getSize()).isEqualTo(new Point(16, 28));
    }

    @Test
    public void intersectsCheck() throws Exception {
        assertThat(a.intersects(b)).isTrue();
        assertThat(a.intersects(new Bounds(new Point(100, 100), new Point(120, 120)))).isFalse();
    }
}
