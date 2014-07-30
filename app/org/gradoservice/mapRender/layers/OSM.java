package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.layers.tiled.TileLayer;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 17:38
 */
public class OSM {
    public static class Mapnik extends TileLayer {
        public static final Options DEFAULT_OPTIONS = new Options();
        {
            DEFAULT_OPTIONS.subdomans = "abc";
        }
        public Mapnik() {
            super("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",DEFAULT_OPTIONS);
        }
    }
}
