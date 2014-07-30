package org.gradoservice.mapRender.geometry;

import org.apache.commons.lang3.SerializationUtils;
import org.gradoservice.mapRender.geo.LatLng;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 17:52
 */
public class Bounds {
    private Point min;
    private Point max;

    public Bounds() {

    }

    public Bounds(Point min, Point max) {
        this.extend(min);
        this.extend(max);
    }

    public Bounds(double[][] points) {
        if (points!=null && points.length==2 && points[0].length==2) {
            Point min = new Point(points[0][0], points[0][1]);
            Point max = new Point(points[1][0], points[1][1]);
            this.extend(min);
            this.extend(max);
        }
    }

    public Point getCenter() { // (Boolean) -> Point
        return getCenter(false);
    }

    public Point getCenter(boolean round) { // (Boolean) -> Point
        return new Point(
                (this.min.getX() + this.max.getX()) / 2,
                (this.min.getY() + this.max.getY()) / 2, round);
    }

    public Point getBottomLeft() { // -> Point
        return new Point(this.min.getX(), this.max.getY());
    }

    public Point getTopRight() { // -> Point
        return new Point(this.max.getX(), this.min.getY());
    }

    public Point getMin() {
        return min;
    }

    public void setMin(Point min) {
        this.min = min;
    }

    public Point getMax() {
        return max;
    }

    public void setMax(Point max) {
        this.max = max;
    }

    public Point getSize() {
        return this.max.subtract(this.min);
    }

    public Bounds extend(Point point) { // (Point)

        if (this.min==null && this.max==null) {
            this.min = SerializationUtils.clone(point);
            this.max = SerializationUtils.clone(point);
        } else {
            this.min.setX(Math.min(point.getX(), this.min.getX()));
            this.max.setX(Math.max(point.getX(), this.max.getX()));
            this.min.setY(Math.min(point.getY(), this.min.getY()));
            this.max.setY(Math.max(point.getY(), this.max.getY()));
        }
        return this;
    }

    public boolean contains(Point point) {
        return contains(point,point);
    }

    public boolean contains(Bounds bounds) {
        Point min = bounds.min;
        Point max = bounds.max;

        return contains(min,max);
    }

    public boolean contains(Point min,Point max) { // (Bounds) or (Point) -> Boolean
        return (min.getX() >= this.min.getX()) &&
                (max.getX() <= this.max.getX()) &&
                (min.getY() >= this.min.getY()) &&
                (max.getY() <= this.max.getY());
    }

    public boolean intersects(Bounds bounds) { // (Bounds) -> Boolean

        Point min = this.min;
        Point max = this.max;
        Point min2 = bounds.min;
        Point max2 = bounds.max;
        boolean xIntersects = (max2.getX() >= min.getX()) && (min2.getX() <= max.getX());
        boolean yIntersects = (max2.getY() >= min.getY()) && (min2.getY() <= max.getY());

        return xIntersects && yIntersects;
    }

    boolean isValid() {
        return this.min!=null && this.max!=null;
    }
}
