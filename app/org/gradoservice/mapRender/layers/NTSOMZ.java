package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.layers.tiled.TileLayer;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 17:14
 */
public class NTSOMZ extends TileLayer {
    public NTSOMZ() {
        super("http://geoportal.ntsomz.ru/get_tile_external.php?x={x}&y={y}&scale={z}");
    }
}
