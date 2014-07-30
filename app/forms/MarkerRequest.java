package forms;

import helpers.Utils;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.layers.marker.Marker;
import play.data.validation.Constraints;

import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 15:47
 */
public class MarkerRequest implements ILayerRequest {
    public String icon;
    @Constraints.Required
    public LatLng latLng;

    public Marker getLayer() {
       return new Marker(latLng);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
