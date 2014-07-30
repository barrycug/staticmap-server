package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.crs.CRSTest;
import org.gradoservice.mapRender.geo.crs.EPSG3857;
import org.gradoservice.mapRender.geometry.Point;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.gradoservice.mapRender.assertions.Assertions.assertLatLngThat;
import static org.gradoservice.mapRender.assertions.Assertions.assertPointThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class EPSG3857Test extends CRSTest<EPSG3857> {

    public EPSG3857Test() {
        super(EPSG3857.getInstance());
    }

    @Test
    public void latLngToPointCheck() {
        assertPointThat(crs.latLngToPoint(new LatLng(0, 0), 0)).as("projects a center point").near(new Point(128, 128), 0.01);
        assertPointThat(crs.latLngToPoint(new LatLng(85.0511287798, 180), 0)).as("projects the northeast corner of the world").near(new Point(256, 0));
    }

    @Test
    public void pointToLatLngCheck() {
        assertLatLngThat(crs.pointToLatLng(new Point(128, 128), 0)).as("reprojects a center point").near(new LatLng(0, 0), 0.01);
        assertLatLngThat(crs.pointToLatLng(new Point(256, 0), 0)).as("reprojects the northeast corner of the world").near(new LatLng(85.0511287798, 180));
    }

    @Test
    public void projectCheck() {
        assertPointThat(crs.project(new LatLng(50, 30))).near(new Point(3339584.7238, 6446275.84102));
        assertPointThat(crs.project(new LatLng(85.0511287798, 180))).near(new Point(20037508.34279, 20037508.34278));
        assertPointThat(crs.project(new LatLng(-85.0511287798, -180))).near(new Point(-20037508.34279, -20037508.34278));
    }

    @Test
    public void unprojectCheck() {
        assertLatLngThat(crs.unproject(new Point(3339584.7238, 6446275.84102))).near(new LatLng(50, 30));
        assertLatLngThat(crs.unproject(new Point(20037508.34279, 20037508.34278))).near(new LatLng(85.051129, 180));
        assertLatLngThat(crs.unproject(new Point(-20037508.34279, -20037508.34278))).near(new LatLng(-85.051129, -180));
    }

    @Test
    public void getProjectedBoundsCheck() {
        int worldSize =256;
        int i = 0;
        Point crsSize;
        for (i = 0; i <= 22; i++) {
            crsSize = crs.getProjectedBounds(i).getSize();
            assertThat(crsSize.getX()).isEqualTo(worldSize);
            assertThat(crsSize.getY()).isEqualTo(worldSize);
            worldSize *= 2;
        }

    }

    @Test
    public void wrapLatLngCheck() {
        assertThat(crs.wrapLatLng(new LatLng(0, 190)).getLng()).isEqualTo(-170);
        assertThat(crs.wrapLatLng(new LatLng(0, 360)).getLng()).isEqualTo(0);
        assertThat(crs.wrapLatLng(new LatLng(0, 380)).getLng()).isEqualTo(20);
        assertThat(crs.wrapLatLng(new LatLng(0, -190)).getLng()).isEqualTo(170);
        assertThat(crs.wrapLatLng(new LatLng(0, -360)).getLng()).isEqualTo(0);
        assertThat(crs.wrapLatLng(new LatLng(0, -380)).getLng()).isEqualTo(-20);
        assertThat(crs.wrapLatLng(new LatLng(0, 90)).getLng()).isEqualTo(90);
        assertThat(crs.wrapLatLng(new LatLng(0, 180)).getLng()).isEqualTo(180);
    }
}
