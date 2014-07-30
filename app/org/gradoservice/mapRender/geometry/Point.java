package org.gradoservice.mapRender.geometry;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.utils.Util;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 16:40
 */
public class Point implements Serializable {
    private double x;
    private double y;
    private double z;
    private int code;

    public Point() {
        this.x = 0;
        this.y = 0;
    }


    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public Point(double x, double y) {
        this(x,y,false);
    }

    public Point(double x, double y, boolean round) {
        this.x = round ? Math.round(x) : x;
        this.y = round ? Math.round(y) : y;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LatLng toLatLng() {
        return new LatLng(y,x);
    }

    public Point add(Point point) {
        return SerializationUtils.clone(this)._add(point);
    }



    // destructive, used directly for performance in situations where it's safe to modify existing point
    public Point _add(Point point) {
        this.x += point.x;
        this.y += point.y;
        return this;
    }

    public Point subtract(Point point) {
        return SerializationUtils.clone(this)._subtract(point);
    }

    public Point _subtract(Point point) {
        this.x -= point.x;
        this.y -= point.y;
        return this;
    }

    public Point divideBy(double num) {
        return SerializationUtils.clone(this)._divideBy(num);
    }

    public Point _divideBy(double num) {
        this.x /= num;
        this.y /= num;
        return this;
    }

    public Point multiplyBy(double num) {
        return SerializationUtils.clone(this)._multiplyBy(num);
    }

    public Point _multiplyBy(double num) {
        this.x *= num;
        this.y *= num;
        return this;
    }

    public Point round() {
        return SerializationUtils.clone(this)._round();
    }

    public Point _round() {
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        return this;
    }

    public Point floor() {
        return SerializationUtils.clone(this)._floor();
    }

    public Point _floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        return this;
    }

    public Point ceil() {
        return SerializationUtils.clone(this)._ceil();
    }

    public Point _ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        return this;
    }

    public double distanceTo(Point point) {

        double x = point.x - this.x,
                y = point.y - this.y;

        return Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        if (Double.compare(point.y, y) != 0) return false;
        if (Double.compare(point.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int precision) {
        String[] parts = {"Point(", Util.formatNum(this.x, precision).toString(), ", ", Util.formatNum(this.y, precision).toString(), ")"};
        return StringUtils.join(parts);
    }


}
