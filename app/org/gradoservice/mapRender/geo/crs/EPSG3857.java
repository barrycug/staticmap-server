package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.projection.LonLat;
import org.gradoservice.mapRender.geo.projection.SphericalMercator;
import org.gradoservice.mapRender.geometry.Transformation;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 18:20
 */
public class EPSG3857 extends Earth {

    private static volatile EPSG3857 instance;

    public static EPSG3857 getInstance() {
        EPSG3857 localInstance = instance;
        if (localInstance == null) {
            synchronized (EPSG3857.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EPSG3857();
                }
            }
        }
        return localInstance;
    }

    public EPSG3857() {
        super();
        // 0.5 / (Math.PI * L.Projection.SphericalMercator.R);
        double scale = 0.5 / (Math.PI * SphericalMercator.R);
        this.name = "EPSG:3857";
        this.projection = SphericalMercator.getInstance();
        this.transformation = new Transformation(scale, 0.5, -scale, 0.5);
    }
}
