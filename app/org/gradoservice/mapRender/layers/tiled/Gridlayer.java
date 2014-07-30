package org.gradoservice.mapRender.layers.tiled;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.geo.crs.CRS;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.utils.Util;
import play.Logger;
import play.libs.Akka;
import play.libs.F;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 19.02.14
 * Time: 19:27
 */
public abstract class Gridlayer extends Layer {
    protected Bounds bounds;
    protected int minZoom = 0;
    protected int maxZoom = 18;
    protected int tileSize;
    private int tilesToLoad = 0;
    private int tilesTotal = 0;
    private Bounds tileNumBounds;
    private double[] wrapLng;
    private double[] wrapLat;

    public Gridlayer(Bounds bounds) {
        this(bounds, 1, 18);
    }

    public Gridlayer(Bounds bounds, int minZoom, int maxZoom) {
        this(bounds, minZoom, maxZoom,256);
    }

    public Gridlayer(Bounds bounds, int minZoom, int maxZoom, int tileSize) {
        this.bounds = bounds;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.tileSize = tileSize;
    }

    @Override
    public Layer addTo(MapComponent mapComponent) {
        Layer layer = super.addTo(mapComponent);
        Point size = this.mapComponent.getSize();
        return layer;
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public void reset() {
        this.tilesToLoad = 0;
        this.tilesTotal = 0;

        this.tileNumBounds = getTileNumBounds();
    }

    private Bounds getTileNumBounds() {
        Bounds bounds = this.mapComponent.getPixelWorldBounds();
        int size = this.getTileSize();
        return new Bounds(bounds.getMin().divideBy(size).floor(),bounds.getMax().divideBy(size).ceil().subtract(new Point(1,1)));
    }

    protected Point getTilePos(Point coords) {
        return coords
                .multiplyBy(this.getTileSize())
                .subtract(this.mapComponent.getPixelOrigin());
    }

    protected void wrapCoords(Point coords) {
        coords.setX(this.wrapLng!=null ? Util.wrapNum(coords.getX(), this.wrapLng, false) : coords.getX());
        coords.setY(this.wrapLat!=null ? Util.wrapNum(coords.getY(), this.wrapLat, false) : coords.getY());
    }

    private void resetWrap() {

        if (mapComponent.getCrs().isInfinite()) { return; }

        if (mapComponent.getCrs().getWrapLng()!=null) {
            this.wrapLng = new double[]{
                Math.floor(mapComponent.project(new LatLng(0, mapComponent.getCrs().getWrapLng()[0])).getX() / tileSize),
                Math.ceil(mapComponent.project(new LatLng(0, mapComponent.getCrs().getWrapLng()[1])).getX() / tileSize)
            };
        }

        if (mapComponent.getCrs().getWrapLat()!=null) {
            this.wrapLat = new double[]{
                Math.floor(mapComponent.project(new LatLng(mapComponent.getCrs().getWrapLat()[0], 0)).getY() / tileSize),
                Math.ceil(mapComponent.project(new LatLng(mapComponent.getCrs().getWrapLat()[1], 0)).getY() / tileSize)
            };
        }
    }

    public F.Promise<MapComponent.RenderResult> render() {
        this.reset();
        if (this.mapComponent ==null) { return emptyImage(); }

        final Bounds bounds = this.mapComponent.getPixelBounds();
        final int zoom = this.mapComponent.getZoom();
        final int tileSize = this.getTileSize();

        if (zoom > this.maxZoom ||
                zoom < this.minZoom) { return emptyImage(); }

        // tile coordinates range for the current view
        final Bounds tileBounds = new Bounds(
                bounds.getMin().divideBy(tileSize).floor(),
                bounds.getMax().divideBy(tileSize).floor());

        F.Promise<List<Tile>> promises = this.addTiles(tileBounds);

        final Gridlayer gridlayer = this;
        final Point size = this.mapComponent.getSize();

        if (promises==null) {
            return emptyImage();
        }  else {
           return promises.map(new F.Function<List<Tile>, MapComponent.RenderResult>() {
               public MapComponent.RenderResult apply(List<Tile> tiles) {
                   MapComponent.RenderResult renderResult = new MapComponent.RenderResult();
                   renderResult.target = gridlayer;
                   renderResult.position = new Point(0,0);
                   renderResult.image = new BufferedImage(new Double(size.getX()).intValue(),new Double(size.getY()).intValue(),BufferedImage.TYPE_INT_ARGB);
                   Graphics2D g2d = renderResult.image.createGraphics();
                   for (Tile tile : tiles) {
                       if (tile.getPosition()!=null && tile.getImage()!=null) {
                           int px = new Double(tile.getPosition().getX()).intValue();
                           int py = new Double(tile.getPosition().getY()).intValue();
                           g2d.drawImage(tile.getImage(), null, px, py);
                       }
                   }
                   g2d.dispose();

                   return renderResult;
               }
           });
        }

    }

    protected F.Promise<MapComponent.RenderResult> emptyImage() {
        return F.Promise.promise(new F.Function0<MapComponent.RenderResult>() {
            @Override
           public MapComponent.RenderResult apply() throws Exception {
               return null;
           }
       });
    }

    private F.Promise<List<Tile>> addTiles(final Bounds tileBouds) {
        final List<Point> queue = new ArrayList<Point>();
        final List<F.Promise<Tile>> promises = new ArrayList<F.Promise<Tile>>();
        final Point center = tileBouds.getCenter();
        final int zoom = this.mapComponent.getZoom();

        for (double j = tileBouds.getMin().getY(); j<=tileBouds.getMax().getY(); j++) {
            for (double i = tileBouds.getMin().getX(); i <= tileBouds.getMax().getX(); i++) {
                Point coords = new Point(i,j);
                coords.setZ(zoom);

                if (this.isValidTile(coords)) {
                    queue.add(coords);
                }
            }
        }

        int tilesToLoad = queue.size();

        if (tilesToLoad == 0) { return null; }

        this.tilesToLoad += tilesToLoad;
        this.tilesTotal += tilesToLoad;

        Collections.sort(queue, new Comparator<Point>() {
            @Override
            public int compare(Point point, Point point2) {
                return new Double(point.distanceTo(center) - point2.distanceTo(center)).intValue();
            }
        });

        for (int i = 0; i < tilesToLoad; i++) {
            try {
                promises.add(this.addTile(queue.get(i)));
            } catch (Exception e) {
                Logger.error(e.getMessage());
            }
        }

        return F.Promise.sequence(promises.toArray(new F.Promise[promises.size()]));

    }

    protected F.Promise<Tile> addTile(Point coords) throws Exception {
        final Point tilePos = this.getTilePos(coords);

        // wrap tile coords if necessary (depending on CRS)
        this.wrapCoords(coords);

        F.Promise<Tile> tilePromise = this.createTile(coords);
        final Gridlayer gridlayer = this;

        return tilePromise.map(
                new F.Function<Tile, Tile>() {
                    public Tile apply(Tile tile) {
                        tile.setPosition(tilePos);
                        gridlayer.tileReady(tile);
                        return tile;
                    }
                }
        );

    }

    private boolean isValidTile(Point coords) {
        CRS crs = this.mapComponent.getCrs();

        if (!crs.isInfinite()) {
            // don't load tile if it's out of bounds and not wrapped
            Bounds bounds = this.tileNumBounds;
            if ((crs.getWrapLng()==null && (coords.getX() < bounds.getMin().getX() || coords.getX() > bounds.getMax().getX())) ||
                    (crs.getWrapLat()==null && (coords.getY() < bounds.getMin().getY() || coords.getY() > bounds.getMax().getY()))) { return false; }
        }

        if (this.bounds==null) { return true; }

        // don't load tile if it doesn't intersect the bounds in options
        LatLngBounds tileBounds = this.tileCoordsToBounds(coords);
        return new LatLngBounds(bounds).intersects(tileBounds);
    }

    private LatLngBounds tileCoordsToBounds(Point coords) {

        Point nwPoint = coords.multiplyBy(tileSize);
        Point sePoint = nwPoint.add(new Point(tileSize, tileSize));

        LatLng nw = mapComponent.wrapLatLng(mapComponent.unproject(nwPoint, new Double(coords.getZ()).intValue()));
        LatLng se = mapComponent.wrapLatLng(mapComponent.unproject(sePoint, new Double(coords.getZ()).intValue()));

        return new LatLngBounds(nw, se);
    }

    public void tileReady(Tile tile) {
        this.tilesToLoad--;
    }


    protected abstract String getTileUrl(final Point coords);

    protected abstract F.Promise<Tile> createTile(final Point coords) throws Exception;

}
