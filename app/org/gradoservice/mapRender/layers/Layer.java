package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.utils.Util;
import play.libs.F;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 19:11
 */
public abstract class Layer {

    protected MapComponent mapComponent;
    protected Integer id;
    protected String name;

    public Layer addTo(MapComponent mapComponent) {

        Integer id = mapComponent.stamp(this);

        if (mapComponent.hasLayer(this)) return this;

        mapComponent.getLayers().put(this.getId(),this);

        mapComponent.getLayerList().add(this);

        this.beforeAdd(mapComponent);

        this.mapComponent = mapComponent;

        this.onAdd(mapComponent);

        return this;
    }

    public Layer remove() {

        Integer id = mapComponent.stamp(this);

        if (!mapComponent.hasLayer(this)) return this;

        mapComponent.getLayers().remove(this.getId());
        mapComponent.getLayerList().remove(this);

        this.onRemove(mapComponent);

        this.mapComponent = null;

        return this;
    }

    public MapComponent getMapComponent() {
        return mapComponent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    protected void beforeAdd(MapComponent mapComponent) {

    }

    protected void onAdd(MapComponent mapComponent) {

    }

    protected void onRemove(MapComponent mapComponent) {

    }

    public String getName() {
        return name;
    }

    public Layer setName(String name) {
        this.name = name;
        return this;
    }

    public abstract F.Promise<MapComponent.RenderResult> render();
}
