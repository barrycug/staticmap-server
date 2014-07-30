package org.gradoservice.mapRender.geo.projection;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 17:15
 */
public class SphericalMercator  implements IProjection {
    public static final double R = 6378137.0;

    private static volatile SphericalMercator instance;

    public static SphericalMercator getInstance() {
        SphericalMercator localInstance = instance;
        if (localInstance == null) {
            synchronized (SphericalMercator.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SphericalMercator();
                }
            }
        }
        return localInstance;
    }

    private final Bounds bounds;

    public SphericalMercator() {
        double d = R * Math.PI;
        double[][] defaultBounds = {{-d, -d},{d, d}};
        bounds = new Bounds(defaultBounds);
    }

    @Override
    public Point project(LatLng latLng) {

        double d = Math.PI / 180;
        double max = 1 - 1E-15;
        double sin =  Math.max(Math.min(Math.sin(latLng.getLat() * d), max), -max);

        return new Point(
                R * latLng.getLng() * d,
                R * Math.log((1 + sin) / (1 - sin)) / 2);
    }

    @Override
    public LatLng unproject(Point point) {

        double d = 180 / Math.PI;

        return new LatLng(
                (2 * Math.atan(Math.exp(point.getY() / R)) - (Math.PI / 2)) * d,
                point.getX() * d / R);
    }

    @Override
    public Bounds bounds() {
        return bounds;
    }
}
