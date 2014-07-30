package org.gradoservice.mapRender.widgets;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.layers.ILegendGraphicLayer;
import org.gradoservice.mapRender.layers.Layer;
import play.Logger;
import play.libs.F;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by germanosin on 20.06.14.
 */
public class LegendGraphic extends Widget {
    private int width = 100;
    private static final Color backgroundColor = new Color(1.0f, 1.0f, 1.0f, 0.7f);
    private final int lineHeight = 20;
    private final int fontHeight = 8;

    public LegendGraphic(int width) {
        super(Position.TOP_RIGHT);
        this.width = width;
    }

    @Override
    public F.Promise<MapComponent.RenderResult> render() {

        final Widget target = this;

        final List<Layer> layerList = this.mapComponent.getLayerList();
        List<F.Promise<BufferedImage>> promises = new ArrayList<F.Promise<BufferedImage>>();
        for (Layer layer : layerList) {
            if (layer instanceof ILegendGraphicLayer) {
                if (((ILegendGraphicLayer) layer).hasLegendGraphic()) {
                    promises.add(((ILegendGraphicLayer) layer).renderLegendGraphic(width, lineHeight, fontHeight));
                }
            }
        }

        return F.Promise.sequence(promises.toArray(new F.Promise[promises.size()])).map(new F.Function<List<BufferedImage>, MapComponent.RenderResult>() {
            public MapComponent.RenderResult apply(List<BufferedImage> layers) {
                int layersCount = layers.size();
                if (layersCount>0) {
                    int height = (layersCount * lineHeight) + lineHeight;
                    MapComponent.RenderResult renderResult = new MapComponent.RenderResult();
                    renderResult.target = target;
                    renderResult.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = renderResult.image.createGraphics();

                    g2d.setColor(backgroundColor);
                    g2d.fillRect(0, 0, width, height);

                    for (int i = 0; i < layersCount; i++) {
                        BufferedImage layer = layers.get(i);
                        if (layer != null) {
                            int py = (i * lineHeight)+10;
                            g2d.drawImage(layer, null, 0, py);
                            //Logger.info("py: " + Integer.toString(py) + " height: " + Integer.toString(layer.getHeight()));
                        }
                    }
                    g2d.dispose();
                    return renderResult;
                } else return null;
            }
        });
    }
}
