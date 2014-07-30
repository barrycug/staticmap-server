package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.layers.tiled.TileLayer;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 14:10
 */
public class Rekod extends TileLayer {
    public Rekod() {
        super("http://basemap.rekod.ru/worldmap/{z}/{x}/{y}.png");
    }
}

