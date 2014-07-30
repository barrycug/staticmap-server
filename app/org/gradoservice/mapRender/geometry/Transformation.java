package org.gradoservice.mapRender.geometry;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 17:23
 */
public class Transformation {
    private double a;
    private double b;
    private double c;
    private double d;

    public Transformation(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Point transform(Point point){
        return this.transformPoint(SerializationUtils.clone(point), 1.0);
    }

    public Point transform(Point point, Double scale){
        return this.transformPoint(SerializationUtils.clone(point), scale);
    }

    public Point transformPoint(Point point, Double scale) {
        scale = scale != null ? scale : 1;


        point.setX(scale * (this.a * point.getX() + this.b));
        point.setY(scale * (this.c * point.getY() + this.d));
        return point;
    }

    public Point untransform(Point point) {
        return untransform(point, 1.0);
    }

    public Point untransform(Point point, Double scale) {
        scale = scale != null ? scale : 1;
        return new Point(
                (point.getX() / scale - this.b) / this.a,
                (point.getY() / scale - this.d) / this.c);
    }
}
