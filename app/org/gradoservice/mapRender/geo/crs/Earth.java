package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.LatLng;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 18:10
 */
public abstract class Earth extends CRS {
    public static final double R = 6378137;

    public Earth() {
        final double[] array =  {-180,180};
        this.wrapLng = array;
    }

    public double distance(LatLng latlng1,LatLng latlng2) {
        double rad = Math.PI / 180;
        double lat1 = latlng1.getLat() * rad;
        double lat2 = latlng2.getLat() * rad;

        return this.R * Math.acos(Math.sin(lat1) * Math.sin(lat2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.cos((latlng2.getLng() - latlng1.getLng()) * rad));
    }
}
