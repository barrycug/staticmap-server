package org.gradoservice.mapRender.geo.projection;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geometry.Point;

import static org.gradoservice.mapRender.assertions.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
public class MercatorProjectionTest extends ProjectionTest<Mercator> {
    public MercatorProjectionTest() {
        super(Mercator.getInstance(), -85.0840591556, 85.0840591556);
    }

    @Override
    public void projectCheck() {
        super.projectCheck();
        assertPointThat(p.project(new LatLng(50, 30))).as("projects other points").near(new Point(3339584, 6413524));
    }
}
