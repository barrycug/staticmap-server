package org.gradoservice.mapRender.layers.vector;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.geometry.PolyUtil;
import play.libs.F;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.02.14
 * Time: 13:26
 */
public class Polygon extends Polyline {

    public Polygon(List<Ring<LatLng>> rings) {
        super(rings);
    }


    public Polygon(List<LatLng> latlngs, Path.Options options) {
        super(latlngs, options);
    }

    @Override
    protected void clipPoints() {
        if (this.options.noClip) {
            this.drawParts = this.drawRings;
            return;
        }

        // polygons need a different clipping algorithm so we redefine that
        Point size = this.mapComponent.getSize();
        Bounds bounds = new Bounds(new Point(0,0), size);
        int w = this.options.weight;
        Point p = new Point(w,w);


        // increase clip padding by stroke width to avoid stroke on clip edges
        bounds = new Bounds(bounds.getMin().subtract(p), bounds.getMax().add(p));

        this.drawParts = new ArrayList<Ring<Point>>();

        int len = this.drawRings.size();

        for (Ring<Point> ring : drawRings) {
            List<Point> clipped = PolyUtil.clipPolygon(ring.getLatLngs(), bounds);
            if (clipped.size()>0) {
                this.drawParts.add(new Ring<Point>(clipped));
            }
        }
    }

    public static void setPaint(Graphics2D g2d, Options options) {
        Color color = Color.decode(options.fillColor);
        int alpha =  new Float(255 * options.fillOpacity).intValue();
        Color fillColor = new Color(color.getRed(),color.getGreen(), color.getBlue(), alpha);
        g2d.setPaint(fillColor);
    }

    public static void drawLegend(Graphics2D g2d, Options options, int lineHeight) {
        final int offset = 5;
        final int max = lineHeight - offset;
        setPaint(g2d, options);
        Polyline.setStroke(g2d, options);
        Path2D path = new Path2D.Float();
        path.moveTo(offset, offset);
        path.lineTo(max, offset);
        path.lineTo(max,max);
        path.lineTo(offset,max);
        path.lineTo(offset,offset);
        path.closePath();
        g2d.draw(path);
    }

    public static void draw(Graphics2D g2d, List<Ring<Point>> parts, Options options) {

        setPaint(g2d, options);

        for (int i = 0; i < parts.size(); i++) {
            Ring<Point> ring = parts.get(i);
            List<Point> points = ring.getLatLngs();
            Path2D path = new Path2D.Double();
            for (int j = 0; j < points.size(); j++) {
                if (j == 0) {
                    path.moveTo(points.get(j).getX(), points.get(j).getY());
                } else {
                    path.lineTo(points.get(j).getX(), points.get(j).getY());
                }
            }
            path.closePath();
            g2d.fill(path);
        }

    }



    @Override
    public F.Promise<MapComponent.RenderResult> render() {

        if (this.mapComponent ==null) { return null; }

        this.project();
        this.clipPoints();
        this.simplifyPoints();

        final int wx = new Double(this.mapComponent.getSize().getX()).intValue();
        final int wy = new Double(this.mapComponent.getSize().getY()).intValue();

        final List<Ring<Point>> parts = this.drawParts;
        final Options options = this.options;
        final Polygon target = this;

        return F.Promise.promise(new F.Function0<MapComponent.RenderResult>() {
            @Override
            public MapComponent.RenderResult apply() throws Exception {
                MapComponent.RenderResult renderResult = new MapComponent.RenderResult();
                renderResult.target = target;
                renderResult.position = new Point(0, 0);
                renderResult.image = new BufferedImage(wx, wy, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = renderResult.image.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                Polygon.draw(g2d, parts, options);
                Polyline.draw(g2d, parts, options);
                g2d.dispose();
                return renderResult;
            }
        });
    }

    @Override
    public F.Promise<BufferedImage> renderLegendGraphic(final int maxWidth, final int lineHeight, final int fontHeight) {
        return F.Promise.promise(new F.Function0<BufferedImage>() {

            @Override
            public BufferedImage apply() throws Throwable {
                BufferedImage image = new BufferedImage(maxWidth, lineHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                //drawLegend(g2d, options, lineHeight);
                Path.drawName(g2d, name, lineHeight, fontHeight);
                g2d.dispose();
                return image;
            }
        });
    }
}
