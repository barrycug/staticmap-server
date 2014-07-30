package org.gradoservice.mapRender.layers.vector;

import org.gradoservice.mapRender.geo.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 18:14
 */
public class Ring<T> {
    private List<T> latLngs;

    public Ring() {
        this.latLngs = new ArrayList<T>();
    }


    public Ring(List<T> latLngs) {
        this.latLngs = latLngs;
    }



    public List<T> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<T> latLngs) {
        this.latLngs = latLngs;
    }
}
