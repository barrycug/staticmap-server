package org.gradoservice.mapRender.layers.tiled;

import com.google.common.collect.ImmutableMap;
import org.gradoservice.mapRender.geo.crs.CRS;
import org.gradoservice.mapRender.geo.crs.EPSG900913;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.utils.Util;
import play.cache.Cache;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 26.02.14
 * Time: 18:34
 */
public class TileLayer extends Gridlayer {

    public static class Options {
          public int tileSize = 256;
          public float opacity = 1.0f;
          public int minZoom = 0;
          public int maxZoom = 18;
          public CRS crs = EPSG900913.getInstance();
          public Bounds bounds = crs.getProjection().bounds();
          public boolean tms = false;
          public String subdomans = "";
          public String errorTileUrl;
          public int zoomOffset = 0;


          public Options() {
          }

          public Options(CRS crs) {
              this.crs = crs;
              this.bounds = crs.getProjection().bounds();
          }

          public Options(CRS crs, Bounds bounds) {
              this.crs = crs;
              this.bounds = bounds;
          }
      }
    private static final Options DEFAULT_OPTIONS = new Options();

    private String url;
    private Options options;

    public TileLayer(String url) {
        this(url, DEFAULT_OPTIONS);
    }

    public TileLayer(String url, Options options) {
        super(options.bounds, options.minZoom, options.maxZoom, options.tileSize);
        this.options = options;
        this.url = url;
    }

    @Override
    protected String getTileUrl(final Point coords) {
        final TileLayer that = this;

        return Util.template(this.url, ImmutableMap.of("s", this.getSubdomain(coords),
                                                        "x", Util.doubleToIntString(coords.getX()),
                                                        "y", Util.doubleToIntString(coords.getY()),
                                                        "z", Util.intToString(this.getZoomForUrl())
                                                        )
        );
    }

    @Override
    protected F.Promise<Tile> createTile(final Point coords) throws Exception {
        final TileLayer layer = this;
        final Tile tile = new Tile(coords);
        tile.setSrc(this.getTileUrl(coords));
        final byte[] bytes = this.loadTileFromCache(tile);
        if (bytes==null) {
           return loadTile(tile);
        } else {
            return F.Promise.promise(new F.Function0<Tile>() {
                @Override
                public Tile apply() throws Exception {
                    try {
                        tile.setImage(ImageIO.read(new ByteArrayInputStream(bytes)));
                        tile.setReady(true);
                    } catch (IOException e) {
                        tile.setError(true);
                    }
                    return tile;
                }
            });
        }
    }

    protected F.Promise<Tile> loadTile(final Tile tile) throws MalformedURLException {
        final TileLayer tileLayer = this;

        return this.createRequest(tile.getSrc()).get().map(
                new F.Function<WSResponse, Tile>() {
                    public Tile apply(WSResponse response) {
                        if (response.getStatus()==200) {
                            try {
                                byte[] bytes = response.asByteArray();
                                tile.setImage(ImageIO.read(new ByteArrayInputStream(bytes)));
                                tile.setReady(true);
                                tileLayer.saveTileToCache(tile,bytes);
                            } catch (IOException e) {
                                tile.setError(true);
                            }
                        }
                        return tile;
                    }
                }
        );
    }

    protected WSRequestHolder createRequest(String tileSrc) throws MalformedURLException {

       URL tileURL = new URL(tileSrc);

       String url = tileURL.getProtocol()+"://"+tileURL.getHost() + ((tileURL.getPort() != -1) ? ":" + tileURL.getPort() : "") + tileURL.getPath();
       WSRequestHolder requestHolder = WS.url(url);
       String userInfo = tileURL.getUserInfo();
       if (userInfo != null) {
           int split = userInfo.indexOf(":");
           String username = userInfo.substring(0, split);
           String password = userInfo.substring(split + 1);
           requestHolder.setAuth(username, password, WSAuthScheme.BASIC);
       }

       String query = tileURL.getQuery();
       if (query!=null) {
           String[] params = query.split("&");
           if (params!=null) {
               for (int i=0;i<params.length;i++) {
                   int split = params[i].indexOf("=");
                   String parameterName  = params[i].substring(0, split);
                   String parameterValue = params[i].substring(split + 1);
                   requestHolder.setQueryParameter(parameterName,parameterValue);
               }
           }

       }

       return requestHolder;
    }

    protected byte[] loadTileFromCache(final Tile tile) {
        return (byte[])Cache.get("tile_"+tile.getSrc());
    }

    protected void saveTileToCache(final Tile tile, byte[] bytes) {
        Cache.set("tile_"+tile.getSrc(), bytes);
    }



    private String getSubdomain(Point tilePoint) {
         if (this.options.subdomans!=null && !this.options.subdomans.isEmpty()) {
            int index = new Double(Math.abs(tilePoint.getX() + tilePoint.getY()) % this.options.subdomans.length()).intValue();
           return this.options.subdomans.substring(index, index+1);
         }
        return "";
    }

    private int getZoomForUrl() {

        int zoom = this.mapComponent.getZoom();
        zoom += this.options.zoomOffset;

        return zoom;
    }
}
