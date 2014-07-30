package org.gradoservice.mapRender.layers.vector;

import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.geometry.Bounds;
import org.gradoservice.mapRender.geometry.LineUtil;
import org.gradoservice.mapRender.geometry.Point;
import play.libs.Akka;
import play.libs.F;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 27.02.14
 * Time: 18:10
 */
public class Polyline extends Path {

    protected List<Ring<LatLng>> rings;
    protected List<Ring<Point>> drawRings;
    protected List<Ring<Point>> drawParts;

    protected LatLngBounds bounds;
    protected Bounds pxBounds;

    public Polyline(List<Ring<LatLng>> rings) {
        super();
        this.setRings(rings);
    }

    public Polyline(List<LatLng> latlngs, Path.Options options) {
        super(options);
        List<Ring<LatLng>> rings = new ArrayList<Ring<LatLng>>();
        Ring ring = new Ring(latlngs);
        rings.add(ring);
        this.setRings(rings);
    }


    public void setRings(List<Ring<LatLng>> rings) {
        this.rings = rings;
        this.bounds = new LatLngBounds();
        for (Ring<LatLng> ring : rings) {
            for (LatLng latLng : ring.getLatLngs()) {
                this.bounds.extend(latLng);
            }
        }
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public Bounds getPxBounds() {
        return pxBounds;
    }

    @Override
    protected void project() {
        this.drawRings = new ArrayList<Ring<Point>>();
        for (Ring<LatLng> ring : rings) {
            List<Point> points = new ArrayList<Point>();
            for (LatLng latLng : ring.getLatLngs()) {
                points.add(this.mapComponent.latLngToLayerPoint(latLng));
            }
            drawRings.add(new Ring<Point>(points));
        }

        this.pxBounds = new Bounds(
                this.mapComponent.latLngToLayerPoint(this.bounds.getSouthWest()),
                this.mapComponent.latLngToLayerPoint(this.bounds.getNorthEast()));
    }

    protected void clipPoints() {
        if (this.options.noClip) {
            this.drawParts = this.drawRings;
            return;
        }

        this.drawParts = new ArrayList<Ring<Point>>();


        Point size = this.mapComponent.getSize();
        Bounds bounds = new Bounds(new Point(0,0), size);


        int k = 0;
        int len = this.drawRings.size();

        for (int i = 0; i < len; i++) {
            List<Point> points = this.drawRings.get(i).getLatLngs();

            int len2 = points.size();

            for (int j =0;j<len2-1; j++) {
                Point[] segment = LineUtil.clipSegment(points.get(j),points.get(j+1), bounds, j>0);
                if (segment==null) continue;

                if (this.drawParts.size()<=k) this.drawParts.add(new Ring<Point>());

                this.drawParts.get(k).getLatLngs().add(segment[0]);

                // if segment goes out of screen, or it's the last one, it's the end of the line part
                if (!segment[1].equals(points.get(j + 1)) || (j == len2 - 2)) {
                    this.drawParts.get(k).getLatLngs().add(segment[1]);
                    k++;
                }
            }
        }
    }

    protected void simplifyPoints() {
        List<Ring<Point>> parts = this.drawParts;
        float tolerance = this.options.smoothFactor;

        int len = parts.size();

        for (int i = 0; i < len; i++) {
            parts.get(i).setLatLngs(LineUtil.simplify(parts.get(i).getLatLngs(), new Double(tolerance)));
        }
    }

    public static void setStroke(Graphics2D g2d,Options options) {
        BasicStroke stroke = new BasicStroke(options.weight, options.getLineJoin(), options.getLineCap());
        g2d.setStroke(stroke);

        Color color = Color.decode(options.color);
        int alpha =  new Float(255 * options.opacity).intValue();
        Color strokeColor = new Color(color.getRed(),color.getGreen(), color.getBlue(), alpha);
        g2d.setPaint(strokeColor);
    }

    public static void drawLegend(Graphics2D g2d, Options options, int lineHeight) {
        final int offset = 5;
        Polyline.setStroke(g2d, options);
        Path2D path = new Path2D.Float();
        path.moveTo(5, lineHeight - offset);
        path.lineTo(lineHeight - offset, 5);
        path.closePath();
        g2d.draw(path);
    }

    public static void draw(Graphics2D g2d, List<Ring<Point>> parts, Options options) {

        setStroke(g2d,options);

        for (int i=0; i<parts.size(); i++) {
            Ring<Point> ring = parts.get(i);
            List<Point> points = ring.getLatLngs();
            Path2D path = new Path2D.Double();
            for (int j=0; j<points.size(); j++) {
                if (j==0) {
                    path.moveTo(points.get(j).getX(), points.get(j).getY());
                } else {
                    path.lineTo(points.get(j).getX(), points.get(j).getY());
                }
            }
            g2d.draw(path);
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
        final Polyline target = this;


        return F.Promise.promise(new F.Function0<MapComponent.RenderResult>() {
            @Override
            public MapComponent.RenderResult apply() throws Exception {
                MapComponent.RenderResult renderResult = new MapComponent.RenderResult();
                renderResult.target = target;
                renderResult.position = new Point(0,0);
                renderResult.image = new BufferedImage(wx,wy, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = renderResult.image.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                Polyline.draw(g2d,parts,options);
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
                drawLegend(g2d, options, lineHeight);
                Path.drawName(g2d, name, lineHeight, fontHeight);
                g2d.dispose();
                return image;
            }
        });
    }
}
