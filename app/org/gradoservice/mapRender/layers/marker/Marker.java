package org.gradoservice.mapRender.layers.marker;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.layers.Layer;
import play.Logger;
import play.libs.Akka;
import play.libs.F;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 03.03.14
 * Time: 12:08
 */
public class Marker extends Layer {
    private Icon icon;
    private LatLng latLng;

    public Marker(LatLng latLng) {
        this(Icon.getDefault(), latLng);
    }

    public Marker(Icon icon, LatLng latLng) {
        this.icon = icon;
        this.latLng = latLng;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Point getPosition() {
        Point position = null;
        if (this.icon!=null && this.latLng!=null && this.mapComponent!=null) {
            position = this.mapComponent.latLngToLayerPoint(this.latLng);
        }
        return position;
    }

    @Override
    public F.Promise<MapComponent.RenderResult> render() {
        if (this.mapComponent==null) return null;
        final Point size = this.mapComponent.getSize();
        final Point position = this.getPosition();
        final Icon icon = this.icon;
        final Marker target = this;
        return F.Promise.promise(new F.Function0<MapComponent.RenderResult>() {
            @Override
            public MapComponent.RenderResult apply() throws Exception {
                MapComponent.RenderResult renderResult = new MapComponent.RenderResult();
                renderResult.target = target;
                try {
                    if (!icon.isInited()) icon.initialize();
                    renderResult.image = icon.getImage();
                    renderResult.position = position.subtract(icon.getAnchor());
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                }
                return renderResult;
            }
        });
    }
}
