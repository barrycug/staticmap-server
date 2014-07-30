package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.crs.CRSTest;
import org.gradoservice.mapRender.geo.crs.EPSG3395;
import org.gradoservice.mapRender.geometry.Point;
import org.junit.Test;

import static org.gradoservice.mapRender.assertions.Assertions.assertLatLngThat;
import static org.gradoservice.mapRender.assertions.Assertions.assertPointThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public class EPSG3395Test extends CRSTest<EPSG3395> {
    public EPSG3395Test() {
        super(EPSG3395.getInstance());
    }

    @Test
    public void latLngToPointCheck() {
        assertPointThat(crs.latLngToPoint(new LatLng(0, 0), 0)).as("projects a center point").near(new Point(128, 128), 0.01);
        assertPointThat(crs.latLngToPoint(new LatLng(85.0840591556, 180), 0)).as("projects the northeast corner of the world").near(new Point(256, 0));
    }

    @Test
    public void pointToLatLngCheck() {
        assertLatLngThat(crs.pointToLatLng(new Point(128, 128), 0)).as("reprojects a center point").near(new LatLng(0, 0), 0.01);
        assertLatLngThat(crs.pointToLatLng(new Point(256, 0), 0)).as("reprojects the northeast corner of the world").near(new LatLng(85.0840591556, 180));
    }
}
