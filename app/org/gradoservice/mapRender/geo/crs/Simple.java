package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.projection.LonLat;
import org.gradoservice.mapRender.geometry.Transformation;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 18:07
 */
public abstract class Simple extends CRS {
    public Simple() {
        this.projection = LonLat.getInstance();
        this.transformation = new Transformation(1,0,-1,0);

        this.infinite = true;
    }

    public double scale(int zoom) {
        return Math.pow(2, zoom);
    }

    public double distance(LatLng latlng1,LatLng latlng2) {
        double dx = latlng2.getLng() - latlng1.getLng(),
                dy = latlng2.getLat() - latlng1.getLat();

        return Math.sqrt(dx * dx + dy * dy);
    }
}
