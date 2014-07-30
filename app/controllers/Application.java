package controllers;

import forms.MapRequest;
import forms.MapSize;
import org.gradoservice.mapRender.MapComponent;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.widgets.LegendGraphic;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Application extends Controller {


    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static F.Promise<Result> image() throws IllegalAccessException, InstantiationException {
        Http.RequestBody body = request().body();
        Map<String,String[]> params = body.asFormUrlEncoded();
        Form<MapRequest> mapForm = Form.form(MapRequest.class).bindFromRequest();
        if (!mapForm.hasErrors())  {
            MapRequest mapRequest = mapForm.get();
            Layer baseLayer = mapRequest.getBaseLayer();
            final MapSize size = mapRequest.size;
            LatLng center = mapRequest.center;
            final Integer scale = mapRequest.scale;

            MapComponent mapComponent = new MapComponent(size.width,size.height, mapRequest.zoom, center);
            baseLayer.addTo(mapComponent);

            if (mapRequest.getLegend()
                    && mapRequest.getLayers()!=null
                    && mapRequest.getLayers().size()>0) {

                LegendGraphic legendGraphic = new LegendGraphic(200);
                legendGraphic.addTo(mapComponent);

            }


            List<Layer> layerList = mapRequest.getLayers();

            for (Layer layer : layerList) {
                layer.addTo(mapComponent);
            }

            if (mapRequest.getFitBounds()) {
                if (mapRequest.geoBounds.isValid()) {
                    mapComponent.fitBounds(mapRequest.getGeoBounds());
                }
            }


            return mapComponent.render(scale).map(
                    new F.Function<BufferedImage, Result>() {
                        public Result apply(BufferedImage image) throws IOException {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(image, "png", baos);
                            baos.flush();
                            byte[] bytes = baos.toByteArray();
                            return ok(bytes).as("image/png");
                        }
                    }
            );

        } else {
            return F.Promise.pure((Result) badRequest(Json.toJson(mapForm.errors())));
        }

    }

}
