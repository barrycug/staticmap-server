package org.gradoservice.mapRender.geo.projection;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 17:06
 */
public class Mercator implements IProjection {

    public static final double R = 6378137.0;
    public static final double R_MINOR = 6356752.314245179;

    private static final double[][] defaultBounds = {{-20037508.34279, -15496570.73972},{20037508.34279, 18764656.23138}};

    private static volatile Mercator instance;

    public static Mercator getInstance() {
        Mercator localInstance = instance;
        if (localInstance == null) {
            synchronized (Mercator.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Mercator();
                }
            }
        }
        return localInstance;
    }


    private final Bounds bounds = new Bounds(defaultBounds);

    @Override
    public Point project(LatLng latLng) {

        double d = Math.PI / 180;
        double r = R;
        double y = latLng.getLat() * d;
        double tmp = R_MINOR / r;
        double e = Math.sqrt(1-tmp*tmp);
        double con = e * Math.sin(y);

        double ts = Math.tan(Math.PI/4 - y/2) / Math.pow((1 - con) / (1 + con), e / 2);

        y = -r * Math.log(Math.max(ts, 1E-10));

        return new Point(latLng.getLng() * d * r, y);
    }

    @Override
    public LatLng unproject(Point point) {
        double d = 180 / Math.PI;
        double r = R;
        double tmp = R_MINOR / r;
        double e = Math.sqrt(1-tmp*tmp);
        double ts =  Math.exp(-point.getY() / r);
        double phi = Math.PI / 2 - 2 * Math.atan(ts);

        for (double i = 0, dphi = 0.1, con; i < 15 && Math.abs(dphi) > 1e-7; i++) {
            con = e * Math.sin(phi);
            con = Math.pow((1 - con) / (1 + con), e / 2);
            dphi = Math.PI / 2 - 2 * Math.atan(ts * con) - phi;
            phi += dphi;
        }

        return new LatLng(phi * d, point.getX() * d / r);
    }




    @Override
    public Bounds bounds() {
        return bounds;
    }
}
