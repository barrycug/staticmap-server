package org.gradoservice.mapRender.geometry;

import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class PointCheck {
    @Test
    public void constructorCheck() {
        Point p = new Point(1.5, 2.5);
        assertThat(p.getX()).isEqualTo(1.5);
        assertThat(p.getY()).isEqualTo(2.5);

        p = new Point(1.3, 2.7, true);
        assertThat(p.getX()).isEqualTo(1);
        assertThat(p.getY()).isEqualTo(3);
    }

    @Test
    public void substructCheck() throws Exception {
        Point a = new Point(50, 30),
              b = new Point(20, 10);
        assertThat(a.subtract(b)).isEqualTo(new Point(30, 20));
    }

    @Test
    public void addCheck() throws Exception {
        assertThat(new Point(50, 30).add(new Point(20, 10))).isEqualTo(new Point(70, 40));
    }

    @Test
    public void divideByCheck() throws Exception {
        assertThat(new Point(50, 30).divideBy(5)).isEqualTo(new Point(10, 6));
    }

    @Test
    public void multiplyByCheck() throws Exception {
        assertThat(new Point(50, 30).multiplyBy(2)).isEqualTo(new Point(100, 60));
    }

    @Test
    public void floorCheck() throws Exception {
        assertThat(new Point(50.56, 30.123).floor()).isEqualTo(new Point(50, 30));
    }

    @Test
    public void distanceToCheck() throws Exception {
        Point p1 = new Point(0, 30);
        Point p2 = new Point(40, 0);
        assertThat(p1.distanceTo(p2)).isEqualTo(50.0);
    }

    @Test
    public void equalsCheck() throws Exception {
        Point p1 = new Point(20.4, 50.12);
        Point p2 = new Point(20.4, 50.12);
        Point p3 = new Point(20.5, 50.13);

        assertThat(p1.equals(p2)).isEqualTo(true);
        assertThat(p1.equals(p3)).isEqualTo(false);
    }

    @Test
    public void toStringCheck() throws Exception {
        assertThat(new Point(50, 30).toString()).isEqualTo("Point(50.0, 30.0)");
    }
}
