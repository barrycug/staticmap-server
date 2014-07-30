package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.geo.projection.IProjection;
import org.gradoservice.mapRender.geometry.Transformation;
import org.gradoservice.mapRender.utils.Util;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 16:54
 */
public abstract class CRS {

    protected boolean infinite = false;
    protected double wrapLng[] = null;
    protected double wrapLat[] = null;
    protected Transformation transformation;
    protected IProjection projection;

    protected String name;

    public Point latLngToPoint(LatLng latlng, int zoom) {
        Point projectedPoint = this.projection.project(latlng);
        double scale = this.scale(zoom);

        return this.transformation.transformPoint(projectedPoint, scale);
    }

    // converts pixel coords to geo coords
    public LatLng pointToLatLng(Point point, int zoom) {
         double scale = this.scale(zoom);
         Point untransformedPoint = this.transformation.untransform(point, scale);

        return this.projection.unproject(untransformedPoint);
    }

    // converts geo coords to projection-specific coords (e.g. in meters)
    public Point project(LatLng latlng) {
        return this.projection.project(latlng);
    }

    // converts projected coords to geo coords
    public LatLng unproject(Point point) {
        return this.projection.unproject(point);
    }

    // defines how the world scales with zoom
    public double scale(int zoom) {
        return 256 * Math.pow(2, zoom);
    }

    // returns the bounds of the world in projected coords if applicable
    public Bounds getProjectedBounds(int zoom) {
        if (this.infinite) { return null; }

        Bounds b = this.projection.bounds();
        double s = this.scale(zoom);
        Point min = this.transformation.transform(b.getMin(), s);
        Point max = this.transformation.transform(b.getMax(), s);

        return new Bounds(min, max);
    }

    // whether a coordinate axis wraps in a given range (e.g. longitude from -180 to 180); depends on CRS
    // wrapLng: [min, max],
    // wrapLat: [min, max],

    // if true, the coordinate space will be unbounded (infinite in all directions)
    // infinite: false,

    // wraps geo coords in certain ranges if applicable

    public LatLng wrapLatLng(LatLng latlng) {
        double lng = this.wrapLng!=null ? Util.wrapNum(latlng.getLng(), this.wrapLng, true) : latlng.getLng();
        double lat = this.wrapLat!=null ? Util.wrapNum(latlng.getLat(), this.wrapLat, true) : latlng.getLat();

        return new LatLng(lat, lng);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public double[] getWrapLng() {
        return wrapLng;
    }

    public double[] getWrapLat() {
        return wrapLat;
    }

    public IProjection getProjection() {
        return projection;
    }

    public abstract double distance(LatLng latlng1,LatLng latlng2);
}
