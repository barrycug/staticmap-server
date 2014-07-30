package org.gradoservice.mapRender.geo.projection;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 16:56
 */
public class LonLat implements IProjection {
    private static final double[][] defaultBounds = {{-180, -90},{180, 90}};

    private static volatile LonLat instance;

    public static LonLat getInstance() {
        LonLat localInstance = instance;
        if (localInstance == null) {
            synchronized (LonLat.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new LonLat();
                }
            }
        }
        return localInstance;
    }

    private final Bounds bounds = new Bounds(defaultBounds);

    public LonLat() {
    }

    @Override
    public Point project(LatLng latLng) {
        return new Point(latLng.getLng(), latLng.getLat());
    }

    @Override
    public LatLng unproject(Point point) {
        return new LatLng(point.getY(), point.getX());
    }

    @Override
    public Bounds bounds() {
        return bounds;
    }
}
