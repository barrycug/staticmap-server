package org.gradoservice.mapRender.geo.projection;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.projection.IProjection;
import org.gradoservice.mapRender.geo.projection.Mercator;
import org.gradoservice.mapRender.geometry.Point;
import org.junit.Test;

import static org.gradoservice.mapRender.assertions.Assertions.assertPointThat;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProjectionTest<T extends IProjection> {
    protected final T p;
    protected final Double minY;
    protected final Double maxY;

    protected ProjectionTest(T p, Double minY, Double maxY) {
        this.p = p;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Test
    public void projectCheck() {

        assertPointThat(p.project(new LatLng(0,0))).as("projects a center point").near(new Point(0, 0));

        assertPointThat(p.project(new LatLng(maxY, 180))).as("projects the northeast corner of the world").near(new Point(20037508, 20037508));

        assertPointThat(p.project(new LatLng(minY, -180))).as("projects the southwest corner of the world").near(new Point(-20037508, -20037508));


    }

    private Point pr(Point point) {
        return p.project(p.unproject(point));
    }

    @Test
    public void unprojectCheck() {

        assertPointThat(pr(new Point(0, 0))).near(new Point(0, 0));

        assertPointThat(pr(new Point(-Math.PI, Math.PI))).near(new Point(-Math.PI, Math.PI));
        assertPointThat(pr(new Point(-Math.PI, -Math.PI))).near(new Point(-Math.PI, -Math.PI));

        assertPointThat(pr(new Point(0.523598775598, 1.010683188683))).near(new Point(0.523598775598, 1.010683188683));
    }
}
