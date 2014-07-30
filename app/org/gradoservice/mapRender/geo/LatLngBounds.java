package org.gradoservice.mapRender.geo;

import org.apache.commons.lang3.StringUtils;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.utils.Util;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 16:45
 */
public class LatLngBounds {
    private LatLng southWest;
    private LatLng northEast;

    public LatLngBounds() {
    }

    public LatLngBounds(double[][] points) {
        if (points!=null && points.length==2 && points[0].length==2) {
            LatLng min = new LatLng(points[0][0], points[0][1]);
            LatLng max = new LatLng(points[1][0], points[1][1]);
            this.extend(min);
            this.extend(max);
        }
    }

    public LatLngBounds(LatLng southWest, LatLng northEast) {
        this.extend(southWest);
        this.extend(northEast);
    }

    public LatLngBounds(Point southWest, Point northEast) {
        this.extend(southWest.toLatLng());
        this.extend(northEast.toLatLng());
    }

    public LatLngBounds(Bounds bounds) {
       this(bounds.getMax(), bounds.getMin());
    }

    public LatLngBounds extend(LatLng latLng) {
        if (latLng==null) { return this; }


        if (this.southWest==null && this.northEast==null) {
            this.southWest = new LatLng(latLng.getLat(), latLng.getLng());
            this.northEast = new LatLng(latLng.getLat(), latLng.getLng());
        } else {
            this.southWest.setLat(Math.min(latLng.getLat(), this.southWest.getLat()));
            this.southWest.setLng(Math.min(latLng.getLng(), this.southWest.getLng()));

            this.northEast.setLat(Math.max(latLng.getLat(), this.northEast.getLat()));
            this.northEast.setLng(Math.max(latLng.getLng(), this.northEast.getLng()));
        }

        return this;
    }

    public LatLngBounds extend(LatLngBounds bounds) {
          LatLng sw2 = bounds.getSouthWest();
          LatLng ne2 = bounds.getNorthEast();

          if (sw2!=null && ne2 !=null) {
              this.extend(sw2);
              this.extend(ne2);
          }

         return this;
    }

    public LatLng getCenter() {
        return new LatLng(
                (this.southWest.getLat() + this.northEast.getLat()) / 2,
                (this.southWest.getLng() + this.northEast.getLng()) / 2);
    }

    public LatLng getSouthWest() {
        return southWest;
    }

    public void setSouthWest(LatLng southWest) {
        this.southWest = southWest;
    }

    public LatLng getNorthEast() {
        return northEast;
    }

    public void setNorthEast(LatLng northEast) {
        this.northEast = northEast;
    }

    public Double getWest() {
        return this.southWest.getLng();
    }

    public Double getSouth() {
        return this.southWest.getLat();
    }

    public Double getEast() {
        return this.northEast.getLng();
    }

    public Double getNorth() {
        return this.northEast.getLat();
    }

    public LatLng getNorthWest() {
        return new LatLng(this.getNorth(), this.getWest());
    }

    public LatLng getSouthEast() {
        return new LatLng(this.getSouth(), this.getEast());
    }

    public boolean contains(LatLngBounds bounds) {
        LatLng sw = this.getSouthWest();
        LatLng ne = this.getNorthEast();
        LatLng sw2 = bounds.getSouthWest();
        LatLng ne2 = bounds.getNorthEast();

        return (sw2.getLat() >= sw.getLat()) && (ne2.getLat() <= ne.getLat()) &&
                (sw2.getLng() >= sw.getLng()) && (ne2.getLng() <= ne.getLng());
    }

    public boolean contains(LatLng latLng) {
        LatLng sw = this.getSouthWest();
        LatLng ne = this.getNorthEast();

        LatLng sw2 = latLng;
        LatLng ne2 = latLng;

        return (sw2.getLat() >= sw.getLat()) && (ne2.getLat() <= ne.getLat()) &&
                (sw2.getLng() >= sw.getLng()) && (ne2.getLng() <= ne.getLng());
    }

    public boolean intersects(LatLngBounds bounds) { // (LatLngBounds)

        LatLng sw = this.southWest;
        LatLng ne = this.northEast;
        LatLng sw2 = bounds.getSouthWest();
        LatLng ne2 = bounds.getNorthEast();

        boolean latIntersects = (ne2.getLat() >= sw.getLat()) && (sw2.getLat() <= ne.getLat());
        boolean lngIntersects = (ne2.getLng() >= sw.getLng()) && (sw2.getLng() <= ne.getLng());

        return latIntersects && lngIntersects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LatLngBounds that = (LatLngBounds) o;

        if (northEast != null ? !northEast.equals(that.northEast) : that.northEast != null) return false;
        if (southWest != null ? !southWest.equals(that.southWest) : that.southWest != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = southWest != null ? southWest.hashCode() : 0;
        result = 31 * result + (northEast != null ? northEast.hashCode() : 0);
        return result;
    }

    public String toBBoxString() {
        String[] parts = {this.getWest().toString(), this.getSouth().toString(), this.getEast().toString(), this.getNorth().toString()};
        return StringUtils.join(parts, ",");
    }

    public boolean isValid() {
        return this.southWest!=null && this.northEast!=null;
    }
}
