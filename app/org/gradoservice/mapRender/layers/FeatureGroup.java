package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.layers.marker.Marker;
import org.gradoservice.mapRender.layers.vector.Path;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.02.14
 * Time: 15:44
 */

public class FeatureGroup extends LayerGroup {
    public FeatureGroup() {
        super();
    }

    public FeatureGroup(List<Layer> layers) {
        super(layers);
    }

    public LatLngBounds getBounds() {
        LatLngBounds bounds = new LatLngBounds();
        for (Map.Entry<Integer, Layer> entry : this.layers.entrySet()) {
            Layer layer = entry.getValue();
            if (layer instanceof Path) {
                bounds.extend(((Path) layer).getBounds());
            } else if (layer instanceof Marker) {
                bounds.extend(((Marker) layer).getLatLng());
            }
        }
        return bounds;
    }
}
