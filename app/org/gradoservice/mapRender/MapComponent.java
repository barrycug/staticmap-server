package org.gradoservice.mapRender;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.geo.crs.CRS;
import org.gradoservice.mapRender.geo.crs.EPSG900913;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.widgets.Widget;
import play.libs.F;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 16:33
 */

public class MapComponent {
    private int zoom;

    private static final int DEFAULT_ZOOM_MIN = 1;
    private static final int DEFAULT_ZOOM_MAX = 18;

    private int minZoom = DEFAULT_ZOOM_MIN;
    private int maxZoom = DEFAULT_ZOOM_MAX;

    private LatLng center;
    private Point offset = new Point(0,0);
    private Point initialTopLeftPoint = new Point(0,0);
    private Point size;


    protected CRS crs = EPSG900913.getInstance();

    protected java.util.Map<Integer, Layer> layers = new HashMap<Integer, Layer>();
    protected List<Layer> layerList = new ArrayList<Layer>();
    protected java.util.Map<Integer,Widget> widgets = new HashMap<Integer, Widget>();

    public MapComponent(int width, int height, int zoom, LatLng center) {
        this(width, height, zoom, DEFAULT_ZOOM_MIN, DEFAULT_ZOOM_MAX, center);
    }

    public MapComponent(int width, int height, int zoom, int minZoom, int maxZoom, LatLng center) {
        this.size = new Point(width,height);
        this.zoom = zoom;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.center = center;
    }

    private void resetView() {
        this.initialTopLeftPoint = this.getNewTopLeftPoint(this.center, this.limitZoom(this.zoom));
    }

    public CRS getCrs() {
        return crs;
    }

    public Point getSize() {
        return new Point(size);
    }

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        // TODO: reset mapComponent!!
        this.zoom = zoom;
    }

    public LatLng getCenter() {
        return center;
    }

    public LatLngBounds getBounds() {
        Bounds bounds = this.getPixelBounds();
        LatLng sw = this.unproject(bounds.getBottomLeft());
        LatLng ne = this.unproject(bounds.getTopRight());

        return new LatLngBounds(sw, ne);
    }

    public Point getPixelOrigin() {
        return this.initialTopLeftPoint;
    }

    private Point getTopLeftPoint() {
        return this.getPixelOrigin().subtract(this.getOffset());
    }

    public Bounds getPixelBounds() {
        Point topLeftPoint = this.getTopLeftPoint();
        return new Bounds(topLeftPoint, topLeftPoint.add(this.getSize()));
    }

    public void fitBounds(LatLngBounds bounds) {
        Integer zoom = this.getBoundsZoom(bounds);
        if (zoom!=null) {
            this.center = bounds.getCenter();
            this.zoom = zoom;
        }
    }

    public Integer getBoundsZoom(LatLngBounds bounds) {
        return getBoundsZoom(bounds, false);
    }

    public Integer getBoundsZoom(LatLngBounds bounds,boolean inside) {

        Point size = this.getSize();
        Integer zoom = this.getMinZoom();
        int maxZoom = this.getMaxZoom();
        LatLng ne = bounds.getNorthEast();
        LatLng sw = bounds.getSouthWest();
        boolean zoomNotFound = true;
        Point nePoint;
        Point swPoint;
        Point boundsSize;

        if (inside) {
            zoom--;
        }

        do {
            zoom++;
            nePoint = this.project(ne, zoom);
            swPoint = this.project(sw, zoom);
            boundsSize = new Point(nePoint.getX() - swPoint.getX(), swPoint.getY() - nePoint.getY());

            if (!inside) {
                zoomNotFound = (boundsSize.getX() <= size.getX()) && (boundsSize.getY() <= size.getY());
            } else {
                zoomNotFound = (boundsSize.getX() < size.getX()) || (boundsSize.getY() < size.getY());
            }
        } while (zoomNotFound && (zoom <= maxZoom));

        if (zoomNotFound && inside) {
            return null;
        }

        return inside ? zoom : zoom - 1;
    }

    public Bounds getPixelWorldBounds() {
        return this.crs.getProjectedBounds(this.getZoom());
    }

    public Point getOffset() {
        return this.offset;
    }

    private Point getNewTopLeftPoint(LatLng center, int zoom) {
        Point viewHalf = this.getSize()._divideBy(2);
        return this.project(center, zoom)._subtract(viewHalf)._round();
    }

    public Point project(LatLng latlng) { // (LatLng[, Number]) -> Point
        return this.project(latlng, this.zoom);
    }

    public Point project(LatLng latlng,int zoom) { // (LatLng[, Number]) -> Point
        return this.crs.latLngToPoint(latlng, zoom);
    }

    public LatLng unproject(Point point) { // (LatLng[, Number]) -> Point
        return this.unproject(point, this.zoom);
    }

    public LatLng unproject(Point point,int zoom) { // (LatLng[, Number]) -> Point
        return this.crs.pointToLatLng(point, zoom);
    }

    public LatLng layerPointToLatLng(Point point) {
        return  this.unproject(point.add(this.getPixelOrigin()));
    }

    public Point latLngToLayerPoint(LatLng latlng) { // (LatLng)
        return this.project(latlng)._round()._subtract(this.getPixelOrigin());
    }

    public LatLng wrapLatLng(LatLng latlng) {
        return this.crs.wrapLatLng(latlng);
    }

    private int limitZoom(int zoom) {
        int min = this.getMinZoom();
        int max = this.getMaxZoom();

        return Math.max(min, Math.min(max, zoom));
    }

    public java.util.Map<Integer, Layer> getLayers() {
        return layers;
    }

    public List<Layer> getLayerList() {
        return layerList;
    }

    public boolean hasLayer(Layer layer) {
        return layer.getId()!=null && layers.containsKey(layer.getId()) ? true : false;
    }

    public MapComponent addLayer(Layer layer) {
        layer.addTo(this);
        return this;
    }

    public MapComponent removeLayer(Layer layer) {
        layer.remove();
        return this;
    }

    public java.util.Map<Integer, Widget> getWidgets() {
        return widgets;
    }

    public boolean hasWidget(Widget widget) {
        return widget.getId()!=null && layers.containsKey(widget.getId()) ? true : false;
    }

    public MapComponent addWidget(Widget widget) {
        widget.addTo(this);
        return this;
    }

    public MapComponent removeWidget(Widget widget) {
        widget.remove();
        return this;
    }

    public static class RenderResult {
        public Object target;
        public BufferedImage image;
        public Point position;
    }

    public AtomicInteger lastId = new AtomicInteger(0);

    public Integer stamp(Layer layer) {
        if (layer.getId()==null) {
            layer.setId(lastId.incrementAndGet());
        }

        return layer.getId();
    }

    public Integer stamp(Widget widget) {
        if (widget.getId()==null) {
            widget.setId(lastId.incrementAndGet());
        }

        return widget.getId();
    }

    public F.Promise<BufferedImage> render() {
        return render(1);
    }

    public F.Promise<BufferedImage> render(final int scale) {
        this.resetView();
        List<F.Promise<RenderResult>> promises = new ArrayList<F.Promise<RenderResult>>();

        for (Layer layer : layerList) {
            F.Promise<RenderResult> promise = layer.render();
            if (promise!=null) promises.add(promise);
        }

        for (Widget widget : widgets.values()) {
            F.Promise<RenderResult> promise = widget.render();
            if (promise!=null) promises.add(promise);
        }

        final int wx = new Double(getSize().getX()).intValue();
        final int wy = new Double(getSize().getY()).intValue();

        return F.Promise.sequence(promises.toArray(new F.Promise[promises.size()])).map(new F.Function<List<RenderResult>, BufferedImage>() {
            public BufferedImage apply(List<RenderResult> layers) {
                BufferedImage bufferedImage = new BufferedImage(wx,wy,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                List<RenderResult> widgets = new ArrayList<RenderResult>();

                for (RenderResult layer : layers) {
                    if (layer!=null && layer.target!=null && layer.target instanceof Layer) {
                        int px = layer.position!=null ? new Double(layer.position.getX()).intValue() : 0;
                        int py = layer.position!=null ? new Double(layer.position.getY()).intValue() : 0;

                        g2d.drawImage(layer.image, null, px, py);
                    } else if (layer!=null && layer.target!=null && layer.target instanceof Widget) {
                        widgets.add(layer);
                    }
                }

                int topLeftOffset = 0;
                int topRightOffset = 0;
                int bottomLeftOffset =0;
                int bottomRightOffset = 0;

                for (RenderResult widgetResult : widgets) {
                    Widget widget = (Widget)widgetResult.target;

                    int px = 0;
                    int py = 0;

                    if (widget.getPosition().equals(Widget.Position.TOP_LEFT)) {
                        px =+ topLeftOffset;
                        topLeftOffset =+ widgetResult.image.getWidth();
                    } else if (widget.getPosition().equals(Widget.Position.TOP_RIGHT)) {
                        topRightOffset =+ widgetResult.image.getWidth();
                        px = wx - topRightOffset;
                    } else if (widget.getPosition().equals(Widget.Position.BOTTOM_LEFT)) {
                        px =+ bottomLeftOffset;
                        py = wy - widgetResult.image.getHeight() - 5;
                        bottomLeftOffset =+ widgetResult.image.getWidth();
                    } else if (widget.getPosition().equals(Widget.Position.BOTTOM_RIGHT)) {
                        bottomRightOffset =+ widgetResult.image.getWidth();
                        px = wx - bottomRightOffset;
                        py = wy - widgetResult.image.getHeight() - 5;
                    }

                    g2d.drawImage(widgetResult.image, null, px, py);

                }

                g2d.dispose();

                if (scale>1) {
                    BufferedImage resizedImage = new BufferedImage(wx * scale, wy * scale, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bufferedImage, 0, 0, wx * scale, wy * scale, null);
                    g.dispose();
                    bufferedImage = resizedImage;
                }

                return bufferedImage;
            }
        });
    }
}
