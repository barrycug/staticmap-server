package org.gradoservice.mapRender.geo.projection;


import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geometry.Point;

import static org.gradoservice.mapRender.assertions.Assertions.assertPointThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */
public class SphericalMercatorProjectionTest extends ProjectionTest<SphericalMercator> {

    public SphericalMercatorProjectionTest() {
        super(SphericalMercator.getInstance(), -85.0511287798, 85.0511287798);
    }

    @Override
    public void projectCheck() {
        super.projectCheck();
        assertPointThat(p.project(new LatLng(50, 30))).near(new Point(3339584, 6446275));
    }
}
