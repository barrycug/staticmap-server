package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.crs.CRS;
import org.gradoservice.mapRender.geometry.Point;
import org.junit.Test;
import static org.gradoservice.mapRender.assertions.Assertions.assertPointThat;
import static org.gradoservice.mapRender.assertions.Assertions.assertLatLngThat;
import static org.fest.assertions.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 21:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class CRSTest<T extends CRS> {
    protected final T crs;

    protected CRSTest(T crs) {
        this.crs = crs;
    }
}
