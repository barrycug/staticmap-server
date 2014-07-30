package org.gradoservice.mapRender.geo.crs;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 18:45
 */
public class EPSG900913 extends EPSG3857 {

    private static volatile EPSG900913 instance;

    public static EPSG900913 getInstance() {
        EPSG900913 localInstance = instance;
        if (localInstance == null) {
            synchronized (EPSG900913.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EPSG900913();
                }
            }
        }
        return localInstance;
    }
}
