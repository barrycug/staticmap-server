package org.gradoservice.mapRender.layers.marker;

import org.gradoservice.mapRender.geometry.Point;
import play.Play;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 03.03.14
 * Time: 11:57
 */
public class Icon {
    private URL url;
    private Point size;
    private Point anchor;
    private BufferedImage image;

    private static volatile Icon defaultInstance;

    public static Icon getDefault() {
        Icon localInstance = defaultInstance;
        if (localInstance == null) {
            synchronized (Icon.class) {
                localInstance = defaultInstance;
                if (localInstance == null) {
                        defaultInstance = localInstance = new Icon();
                }
            }
        }
        return localInstance;
    }

    public Icon() {
        //this(new URL("http://geoportal.prochar.ru/public/images/mapsurfer/marker.png"), new Point(49, 52), new Point(13, 41));
        this(Play.application().resource("public/images/marker-icon.png"), new Point(25, 41), new Point(12, 41));
    }

    public Icon(URL url, Point size, Point anchor) {
        this.url = url;
        this.size = size;
        this.anchor = anchor;
        this.image = null;
    }

    public boolean isInited() {
        return this.image!=null;
    }

    public Point getSize() {
        return size;
    }

    public void setSize(Point size) {
        this.size = size;
    }

    public Point getAnchor() {
        return anchor;
    }

    public void setAnchor(Point anchor) {
        this.anchor = anchor;
    }

    public void initialize() throws IOException {
        synchronized (url) {
            this.image = ImageIO.read(url.openStream());
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
