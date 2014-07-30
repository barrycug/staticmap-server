package org.gradoservice.mapRender.widgets;

import org.gradoservice.mapRender.MapComponent;
import play.libs.F;

/**
 * Created by germanosin on 20.06.14.
 */
public abstract class Widget {
    public static enum Position {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    protected Widget(Position position) {
        this.position = position;
    }

    protected Integer id;
    protected Position position;
    protected MapComponent mapComponent;

    public MapComponent getMapComponent() {
        return mapComponent;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Widget addTo(MapComponent mapComponent) {
        this.mapComponent = mapComponent;

        if (mapComponent.hasWidget(this)) return this;

        mapComponent.getWidgets().put(this.getId(),this);

        return this;
    }

    public Widget remove() {

        Integer id = mapComponent.stamp(this);

        if (!mapComponent.hasWidget(this)) return this;

        mapComponent.getWidgets().remove(this.getId());

        this.mapComponent = null;

        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract F.Promise<MapComponent.RenderResult> render();
}
