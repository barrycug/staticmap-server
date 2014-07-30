package org.gradoservice.mapRender.assertions;

import org.fest.assertions.GenericAssert;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geometry.Point;

import static org.fest.assertions.Formatting.format;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public class LatLngAssert extends GenericAssert<LatLngAssert, Object> {
    final LatLng current;

    protected LatLngAssert(LatLng actual) {
        super(LatLngAssert.class,actual);
        current = actual;
    }

    public LatLngAssert near(LatLng another) {
        return near(another, 1e-4);
    }

    public LatLngAssert near(LatLng another, double delta) {
        if (!checkCoordinate(current.getLng(), another.getLng(), delta))
            fail(format("latlng lng is %s but must be within %s - %s", current.getLng(), another.getLng()-delta, another.getLng()+delta));
        if (!checkCoordinate(current.getLat(), another.getLat(), delta))
            fail(format("latlng lat is %s but must be within %s - %s", current.getLat(), another.getLat()-delta, another.getLat()+delta));

        return this;
    }

    private boolean checkCoordinate(Double coord, Double coord2, double delta) {
        return coord >= coord2 - delta && coord<=coord2 + delta;
    }
}
