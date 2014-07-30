package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.projection.Mercator;
import org.gradoservice.mapRender.geometry.Transformation;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 18:47
 */
public class EPSG3395 extends Earth {

    private static volatile EPSG3395 instance;

    public static EPSG3395 getInstance() {
        EPSG3395 localInstance = instance;
        if (localInstance == null) {
            synchronized (EPSG3395.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EPSG3395();
                }
            }
        }
        return localInstance;
    }

    public EPSG3395() {
        super();
        double scale = 0.5 / (Math.PI * Mercator.R);
        this.name = "EPSG:3395";
        this.projection = Mercator.getInstance();
        this.transformation = new Transformation(scale, 0.5, -scale, 0.5);
    }
}
