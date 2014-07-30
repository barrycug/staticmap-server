package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.projection.LonLat;
import org.gradoservice.mapRender.geometry.Transformation;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 18:14
 */
public class EPSG4326 extends Earth {

    private static volatile EPSG4326 instance;

    public static EPSG4326 getInstance() {
        EPSG4326 localInstance = instance;
        if (localInstance == null) {
            synchronized (EPSG4326.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EPSG4326();
                }
            }
        }
        return localInstance;
    }

    public EPSG4326() {
        super();
        this.name = "EPSG:4326";
        this.projection = LonLat.getInstance();
        this.transformation = new Transformation(1.0 / 180.0, 1, -1.0 / 180.0, 0.5);
    }
}
