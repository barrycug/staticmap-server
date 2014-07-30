package org.gradoservice.mapRender.assertions;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class Assertions {
    public static PointAssert assertPointThat(org.gradoservice.mapRender.geometry.Point point) {
        return new PointAssert(point);
    }
    public static LatLngAssert assertLatLngThat(org.gradoservice.mapRender.geo.LatLng latLng) {
        return new LatLngAssert(latLng);
    }
}
