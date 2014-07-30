package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.utils.Util;
import play.libs.F;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.02.14
 * Time: 15:26
 */
public class LayerGroup extends Layer {
    protected Map<Integer, Layer> layers;

    public LayerGroup() {
        layers = new HashMap<Integer, Layer>();
    }

    public LayerGroup(List<Layer> layers) {
        this();
        for (Layer layer : layers) {
            this.addLayer(layer);
        }
    }

    public LayerGroup addLayer(Layer layer) {
        Integer id = layer.hashCode();

        this.layers.put(id, layer);

        if (this.mapComponent !=null) {
            this.mapComponent.addLayer(layer);
        }

        return this;
    }

    public LayerGroup removeLayer(Layer layer) {
        Integer id = layer.hashCode();

        if (this.layers.containsKey(id)) {
            if (this.mapComponent !=null) {
                this.mapComponent.removeLayer(this.layers.get(id));
            }
            this.layers.remove(layer);
        }
        return this;
    }

    public boolean hasLayer(Layer layer) {
        boolean result = false;
        if (layer!=null) {
            Integer id = layer.hashCode();
            if (this.layers.containsKey(layer)) {
               result = true;
            }
        }
        return result;
    }

    public LayerGroup clearLayers() {
        for (Map.Entry<Integer, Layer> entry : layers.entrySet()) {
            this.removeLayer(entry.getValue());
        }
        return this;
    }

    protected void onAdd(MapComponent map) {
        for (Map.Entry<Integer, Layer> entry : layers.entrySet()) {
            map.addLayer(entry.getValue());
        }
    }

    protected void onRemove(MapComponent map) {
        for (Map.Entry<Integer, Layer> entry : layers.entrySet()) {
            map.removeLayer(entry.getValue());
        }
    }

    @Override
    public F.Promise<MapComponent.RenderResult> render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Layer getLayer(Integer id) {
        return this.layers.get(id);
    }

    public Set<Layer> getLayers() {

        Set<Layer> layers = new HashSet<Layer>();

        for (Map.Entry<Integer, Layer> entry : this.layers.entrySet()) {
             layers.add(entry.getValue());
        }

        return layers;

    }

    public Integer getLayerId(Layer layer) {
        return this.mapComponent.stamp(layer);
    }
}
