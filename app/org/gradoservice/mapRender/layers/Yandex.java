package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.layers.tiled.TileLayer;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 17:52
 */

//
public class Yandex {
    public static class Space extends TileLayer {
        public static final Options DEFAULT_OPTIONS = new Options();
        {
            DEFAULT_OPTIONS.subdomans = "12";
        }
        public Space() {
            super("http://sat0{s}.maps.yandex.net/tiles?l=sat&v=3.151.0&x={x}&y={y}&z={z}&lang=ru_RU",DEFAULT_OPTIONS);
        }
    }
}
