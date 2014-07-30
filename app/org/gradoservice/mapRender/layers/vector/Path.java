package org.gradoservice.mapRender.layers.vector;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.layers.ILegendGraphicLayer;
import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.utils.Util;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 18:02
 */
public abstract class Path extends Layer implements ILegendGraphicLayer {

    public static class Options {

        public Options() {

        }

        public Options(boolean dynamicColor) {
            if (dynamicColor) {
                this.color = Util.randomColor();
            }
        }

        public boolean stroke  = true;
        public String color = "#3388ff";
        public int weight = 4;
        public float opacity = 1.0f;

        public String lineCap = "round";
        public String lineJoin = "round";

        public boolean fill = true;
        public String fillColor = color;
        public float fillOpacity = 0.2f;
        public float smoothFactor = 1.0f;

        public boolean noClip = false;

        public int getLineCap() {
            int cap = 1;
            if (lineCap.equals("round")) {
               cap = BasicStroke.CAP_ROUND;
            } else if (lineCap.equals("square")) {
                cap = BasicStroke.CAP_SQUARE;
            }  else if (lineCap.equals("butt")) {
                cap = BasicStroke.CAP_BUTT;
            }
            return cap;
        }

        public int getLineJoin() {
            int cap = 1;
            if (lineJoin.equals("round")) {
                cap = BasicStroke.JOIN_ROUND;
            } else if (lineJoin.equals("miter")) {
                cap = BasicStroke.JOIN_MITER;
            }  else if (lineJoin.equals("bevel")) {
                cap = BasicStroke.JOIN_BEVEL;
            }
            return cap;
        }
    }

    protected Options options;


    protected Path() {
        this(new Options(true));
    }

    protected Path(Options options) {
        this.options = options;
    }

    public Options getOptions() {
        return options;
    }

    public Path setOptions(Options options) {
        this.options = options;
        return this;
    }

    @Override
    protected void onAdd(MapComponent mapComponent) {

    }

    protected abstract void project();

    public abstract LatLngBounds getBounds();

    @Override
    public boolean hasLegendGraphic() {
        return this.name!=null && !this.name.isEmpty() && !this.name.equals(" ");
    }

    public static void drawName(Graphics2D g2d, String name, final int lineHeight, final int fontHeight) {
        g2d.setColor(Color.black);
        g2d.setFont(new Font( "SansSerif", Font.BOLD, fontHeight ));
        int py =  new Double(Math.round((lineHeight - fontHeight) / 2.0)).intValue();
        g2d.drawString(name, lineHeight, py+fontHeight);
    }
}
