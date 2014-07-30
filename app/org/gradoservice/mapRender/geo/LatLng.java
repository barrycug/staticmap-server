package org.gradoservice.mapRender.geo;

import org.apache.commons.lang3.StringUtils;
import org.gradoservice.mapRender.geo.crs.EPSG4326;
import org.gradoservice.mapRender.geo.crs.Earth;
import org.gradoservice.mapRender.utils.Util;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 16:38
 */
public class LatLng {

    public static final double MAX_MARGIN = 1.0E-9;

    private double lat;
    private double lng;

    public LatLng() {
        this(0,0);
    }

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }


    public LatLng(Long lat, Long lng) {
        this.lat = lat.doubleValue();
        this.lng = lng.doubleValue();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LatLng latLng = (LatLng) o;

        double margin = Math.max(
                Math.abs(this.lat - latLng.lat),
                Math.abs(this.lng - latLng.lng));

        return margin <= MAX_MARGIN;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public String toString() {
        return toString(5);
    }

    public String toString(int precision) {
        String[] parts = {"LatLng(", Util.formatNum(this.lat, precision).toString(), ", ", Util.formatNum(this.lng, precision).toString(), ")"};
        return StringUtils.join(parts);
    }

    public boolean isValid() {
        return ((this.lng<=180) && (this.lng>=-180) && (this.lat<=90) && (this.lat>=-90));
    }

    public double distanceTo(LatLng other) {
        return EPSG4326.getInstance().distance(this, other);
    }

}
