package forms;

import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.layers.vector.Path;
import org.gradoservice.mapRender.layers.vector.Polyline;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 15:49
 */
public class PathRequest implements ILayerRequest {
    public String color = "0x0000ff";
    public String weight = "2";
    @Constraints.Required
    public List<LatLng> latLng;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        try {
            int cw = Integer.parseInt(weight);
        } catch (Exception e) {
            errors.add(new ValidationError("weight", "Weight is not a number"));
        }

        if (latLng==null || latLng.isEmpty() || latLng.size()<2)
            errors.add(new ValidationError("latLng", "Not enough latlngs for path"));

        return errors.isEmpty() ? null : errors;
    }


    public Polyline getLayer() {
        Path.Options options = new Path.Options();
        if (color!=null) options.color = color;
        if (weight!=null) options.weight = Integer.parseInt(weight);
        return new Polyline(latLng,options);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<LatLng> getLatLng() {
        return latLng;
    }

    public void setLatLng(List<LatLng> latLng) {
        this.latLng = latLng;
    }
}
