package org.gradoservice.mapRender.assertions;

import org.fest.assertions.GenericAssert;
import org.gradoservice.mapRender.geometry.Point;
import static org.fest.assertions.Formatting.format;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public class PointAssert extends GenericAssert<PointAssert, Object> {
    final Point current;

    protected PointAssert(Point actual) {
        super(PointAssert.class,actual);
        current = actual;
    }

    public PointAssert near(Point another) {
        return near(another, 1);
    }

    public PointAssert near(Point another, double delta) {
        if (!checkCoordinate(current.getX(), another.getX(), delta))
            fail(format("point x is %s but must be within %s - %s", current.getX(), another.getX()-delta, another.getX()+delta));
        if (!checkCoordinate(current.getY(), another.getY(), delta))
            fail(format("point y is %s but must be within %s - %s", current.getY(), another.getY()-delta, another.getY()+delta));

        return this;
    }

    private boolean checkCoordinate(Double coord, Double coord2, double delta) {
        return coord >= coord2 - delta && coord<=coord2 + delta;
    }


}
