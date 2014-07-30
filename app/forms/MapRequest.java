package forms;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.xpath.internal.operations.Bool;
import factory.BaseMapFactory;
import helpers.Utils;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geo.LatLngBounds;
import org.gradoservice.mapRender.layers.FeatureGroup;
import org.gradoservice.mapRender.layers.GeoJSON;
import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.layers.marker.Marker;
import org.gradoservice.mapRender.layers.vector.Path;
import org.gradoservice.mapRender.layers.vector.Polyline;
import play.data.Form;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 13:04
 */
public class MapRequest {



    @Constraints.Required
    public MapSize size;
    public Integer scale = 1;
    @Constraints.Required
    public Integer zoom;
    @Constraints.Required
    public LatLng center;
    @Constraints.Required
    public String baseMap;
    public List<String> markers;
    public List<String> paths;
    public List<JsonNode> geoJsons;
    public Boolean fitBounds = false;
    public Boolean legend = false;
    public LatLngBounds geoBounds = new LatLngBounds();

    private List<Layer> layersList =  new ArrayList<Layer>();

    public Map<String, List<ValidationError>> validate() {
        Map<String, List<ValidationError>> errors = new HashMap<>();

        if (!BaseMapFactory.getInstance().hasBaseLayer(baseMap)) {
            addSimpleError(errors, "baseMap", new ValidationError("baseMap", "There is no such registered baseMap: "+baseMap));
        }

        if (zoom<0 || zoom>20) {
            addSimpleError(errors, "zoom", new ValidationError("zoom", "Zoom is not valid"));
        }

        if (center==null || !center.isValid()) {
            addSimpleError(errors, "center", new ValidationError("center", "Center is not valid"));
        }

        if (scale==null || scale<1 || scale>4) {
            addSimpleError(errors, "scale", new ValidationError("scale", "Scale is not valid. Must be 1<=scale<=4"));
        }

        if (paths!=null && !paths.isEmpty()) {
            List<ValidationError> pathsErrors = this.parseLayers(paths, PathRequest.class, "latLng[]");
            if (pathsErrors!=null && !pathsErrors.isEmpty())
                errors.put("paths", pathsErrors);
        }

        if (markers!=null && !markers.isEmpty()) {
             List<ValidationError> markerErrors = this.parseLayers(markers, MarkerRequest.class, "latLng");
             if (markerErrors!=null && !markerErrors.isEmpty())
                errors.put("markers", markerErrors);
        }

        if (geoJsons!=null && !geoJsons.isEmpty()) {
            this.parseGeoJsons(geoJsons);
        }

        return errors.isEmpty() ? null : errors;
    }

    private void addSimpleError(Map<String, List<ValidationError>> map, String field, ValidationError error) {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        errors.add(error);
        map.put(field, errors);
    }


    public Layer getBaseLayer() throws InstantiationException, IllegalAccessException {
       return BaseMapFactory.getInstance().getBaseLayer(baseMap);
    }


    public <T extends ILayerRequest> List<ValidationError> parseLayers(List<String> objects, Class formClass, String latLngField) {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (objects!=null && !objects.isEmpty()) {
            for (String objectString : objects) {
                Form<T> objectForm = Utils.getClassForm(formClass, objectString, latLngField);
                if (!objectForm.hasErrors()) {
                    Layer layer = objectForm.get().getLayer();
                    this.extendBounds(layer);
                    layersList.add(layer);
                }
                else
                    mergeFieldErrors(errors, objectForm.errors());
            }
        }

        return errors;
    }

    public void parseGeoJsons(List<JsonNode> geoJsons) {
        if (geoJsons!=null && !geoJsons.isEmpty()) {
            for (JsonNode geoJsonNode : geoJsons) {
                try {
                    GeoJSON geoJSON = new GeoJSON(geoJsonNode);
                    this.extendBounds(geoJSON);
                    layersList.add(geoJSON);
                } catch (IOException e) {

                }
            }
        }
    }

    private void extendBounds(Layer layer) {
        if (layer instanceof Path) {
            geoBounds.extend(((Path) layer).getBounds());
        } else if (layer instanceof Marker) {
            geoBounds.extend(((Marker) layer).getLatLng());
        } else if (layer instanceof FeatureGroup) {
            geoBounds.extend(((FeatureGroup) layer).getBounds());
        }
    }

    private void mergeFieldErrors(List<ValidationError> errors, Map<String, List<ValidationError>> addErrors) {
        for (Map.Entry<String, List<ValidationError>> entry : addErrors.entrySet()) {
            for (ValidationError validationError : entry.getValue()) {
                errors.add(validationError);
            }
        }
    }

    public List<Layer> getLayers() {
        return layersList;
    }


    public MapSize getSize() {
        return size;
    }

    public void setSize(MapSize size) {
        this.size = size;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public String getBaseMap() {
        return baseMap;
    }

    public void setBaseMap(String baseMap) {
        this.baseMap = baseMap;
    }

    public List<JsonNode> getGeoJsons() {
        return geoJsons;
    }

    public void setGeoJsons(List<JsonNode> geoJsons) {
        this.geoJsons = geoJsons;
    }

    public Boolean getFitBounds() {
        return fitBounds;
    }

    public void setFitBounds(Boolean fitBounds) {
        this.fitBounds = fitBounds;
    }

    public LatLngBounds getGeoBounds() {
        return geoBounds;
    }

    public Boolean getLegend() {
        return legend;
    }

    public void setLegend(Boolean legend) {
        this.legend = legend;
    }

    public List<String> getMarkers() {
        return markers;
    }

    public void setMarkers(List<String> markers) {
        this.markers = markers;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
