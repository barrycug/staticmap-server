package org.gradoservice.mapRender.geo;

import org.junit.*;
import static org.fest.assertions.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 19:58
 * To change this template use File | Settings | File Templates.
 */
public class LatLngBoundsTest {

    public LatLngBounds getFirst() {
        return new LatLngBounds(
                new LatLng(14, 12),
                new LatLng(30, 40));
    }

    @Test
    public void constructorCheck() {
        LatLngBounds a = getFirst();
        LatLngBounds b = new LatLngBounds(new LatLng(14, 12),new LatLng(30, 40));
        assertThat(b).isEqualTo(a);
        assertThat(b.getNorthWest()).isEqualTo(new LatLng(30, 12));
    }

    @Test
    public void extendByPointCheck() {
         LatLngBounds a = getFirst();
         a.extend(new LatLng(20, 50));
         assertThat(a.getNorthEast()).isEqualTo(new LatLng(30, 50));
    }

    @Test
    public void extendByBoundsCheck() {
        LatLngBounds a = getFirst();
        double[][] arrayBounds = {{20, 50}, {8, 40}};
        a.extend(new LatLngBounds(arrayBounds));
        assertThat(a.getSouthEast()).isEqualTo(new LatLng(8, 50));
    }

    @Test
    public void extendByEmptyBoundsCheck() {
        LatLngBounds a = getFirst();
        assertThat(a.extend(new LatLngBounds())).isEqualTo(a);
    }

    @Test
    public void getCenterCheck() {
        LatLngBounds a = getFirst();
        assertThat(a.getCenter()).isEqualTo(new LatLng(22, 26));
    }

    @Test
    public void equalsCheck() {
        LatLngBounds a = getFirst();
        double[][] arrayBounds = {{14, 12}, {30, 40}};
        double[][] arrayBoundsOther = {{14, 13}, {30, 40}};
        assertThat(a.equals(new LatLngBounds(arrayBounds))).isEqualTo(true);
        assertThat(a.equals(new LatLngBounds(arrayBoundsOther))).isEqualTo(false);
        assertThat(a.equals(null)).isEqualTo(false);
    }


    @Test
    public void getWestCheck() {
        LatLngBounds a = getFirst();
        assertThat(a.getWest()).isEqualTo(12);
    }

    @Test
    public void getSouthCheck() {
        LatLngBounds a = getFirst();
        assertThat(a.getSouth()).isEqualTo(14);
    }

    @Test
    public void getEastCheck() {
        LatLngBounds a = getFirst();
        assertThat(a.getEast()).isEqualTo(40);
    }

    @Test
    public void getNorthCheck() {
        LatLngBounds a = getFirst();
        assertThat(a.getNorth()).isEqualTo(30);
    }

    @Test
    public void toBBoxStringCheck() {
        LatLngBounds a = getFirst();
        assertThat(a.toBBoxString()).isEqualTo("12.0,14.0,40.0,30.0");
    }

    @Test
    public void  getNorthWestTest() {
        LatLngBounds a = getFirst();
        assertThat(a.getNorthWest()).isEqualTo(new LatLng(a.getNorth(), a.getWest()));
    }

    @Test
    public void  getSouthEastTest() {
        LatLngBounds a = getFirst();
        assertThat(a.getSouthEast()).isEqualTo(new LatLng(a.getSouth(), a.getEast()));
    }

    @Test
    public void containsCheck() {
        LatLngBounds a = getFirst();
        LatLng latLng = new LatLng(16,20);
        LatLng latLng2 = new LatLng(5,20);
        assertThat(a.contains(latLng)).isEqualTo(true);
        assertThat(a.contains(latLng2)).isEqualTo(false);

        double[][] arrayBounds = {{16, 20}, {20, 40}};
        double[][] arrayBounds2 =  {{16, 50}, {8, 40}};
        assertThat(a.contains(new LatLngBounds(arrayBounds))).isEqualTo(true);
        assertThat(a.contains(new LatLngBounds(arrayBounds2))).isEqualTo(false);
    }

    @Test
    public void intersectsCheck() {
        LatLngBounds a = getFirst();

        double[][] arrayBounds = {{16, 20}, {50, 60}};
        double[][] arrayBounds2 =  {{40, 50}, {50, 60}};

        assertThat(a.intersects(new LatLngBounds(arrayBounds))).isEqualTo(true);
        assertThat(a.contains(new LatLngBounds(arrayBounds2))).isEqualTo(false);
    }

}
