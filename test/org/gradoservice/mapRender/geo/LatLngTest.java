package org.gradoservice.mapRender.geo;

import org.junit.*;
import static org.fest.assertions.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class LatLngTest {
    @Test
    public void constructorCheck() {
       LatLng a = new LatLng(55.1, 49.2);
       assertThat(a).isNotNull();
       assertThat(a.getLat()).isEqualTo(55.1);
       assertThat(a.getLng()).isEqualTo(49.2);

        LatLng b = new LatLng(-25, -74);
        assertThat(b).isNotNull();
        assertThat(b.getLat()).isEqualTo(-25);
        assertThat(b.getLng()).isEqualTo(-74);
    }

    @Test
    public void equalsCheck() {
        LatLng a = new LatLng(10, 20);
        LatLng b = new LatLng(10 + 1.0E-10, 20 - 1.0E-10);
        assertThat(a.equals(b)).isEqualTo(true);

    }

    @Test
    public void notEqualsCheck() {
        LatLng a = new LatLng(10, 20);
        LatLng b = new LatLng(10, 23.3);
        assertThat(a.equals(b)).isEqualTo(false);
    }

    @Test
    public void notEqualsOnError() {
        LatLng a = new LatLng(10, 20);
        assertThat(a.equals(null)).isEqualTo(false);
    }

    @Test
    public void isValidCheck() {
        LatLng a = new LatLng(190, 89);
        assertThat(a.isValid()).isEqualTo(false);
    }

    @Test
    public void toStringCheck() {
        LatLng a = new LatLng(10.333333333, 20.2222222);
        assertThat(a.toString(3)).isEqualTo("LatLng(10.333, 20.222)");
    }

    @Test
    public void distanceToCheck() {
        LatLng a = new LatLng(50.5, 30.5);
        LatLng b = new LatLng(50, 1);

        assertThat(Math.abs(Math.round(a.distanceTo(b) / 1000) - 2084) < 5).isEqualTo(true);


        LatLng a1 = new LatLng(50.5, 30.5);
        LatLng b1 = new LatLng(50.5, 30.5);

        assertThat(a1.distanceTo(b1)).isEqualTo(0);
    }

}
