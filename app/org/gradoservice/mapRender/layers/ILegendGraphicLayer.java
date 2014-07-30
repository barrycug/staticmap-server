package org.gradoservice.mapRender.layers;

import org.gradoservice.mapRender.MapComponent;
import play.libs.F;

import java.awt.image.BufferedImage;

/**
 * Created by germanosin on 20.06.14.
 */
public interface ILegendGraphicLayer {
    public boolean hasLegendGraphic();
    public F.Promise<BufferedImage> renderLegendGraphic(final int maxWidth, final int height, final int fontSize);
}
