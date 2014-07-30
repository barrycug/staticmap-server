package org.gradoservice.mapRender.layers.tiled;

import org.gradoservice.mapRender.geometry.Point;
import play.Logger;

import java.awt.image.BufferedImage;


/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 12:28
 */
public class Tile {
    private Point coords;
    private Point position;
    private String src;
    private BufferedImage image = null;
    private boolean ready = false;
    private boolean error = false;

    public Tile(Point coords) {
        this.coords = coords;
    }

    public Point getCoords() {
        return coords;
    }

    public void setCoords(Point coords) {
        this.coords = coords;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        Logger.info("url: "+src);
        this.src = src;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
