package org.gradoservice.mapRender.layers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.geometry.Point;
import org.gradoservice.mapRender.layers.marker.Icon;
import org.gradoservice.mapRender.layers.marker.Marker;
import org.gradoservice.mapRender.layers.vector.Path;
import org.gradoservice.mapRender.layers.vector.Polygon;
import org.gradoservice.mapRender.layers.vector.Polyline;
import org.gradoservice.mapRender.layers.vector.Ring;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.02.14
 * Time: 15:45
 */
public class GeoJSON extends FeatureGroup {

    private Map<String,Object> parameters = new HashMap<String,Object>();
    private JsonNode styleJson = null;

    public GeoJSON(String json) throws IOException {
        super();
        JsonNode geojson = new ObjectMapper().readValue(json, JsonNode.class);
        if (json!=null) {
            this.addData(geojson);
        }
    }

    public GeoJSON(JsonNode json) throws IOException {
          super();
          if (json!=null) {
              this.addData(json);
          }
    }

    protected Map<String,Object> parseParameters(JsonNode feature, String elementName) {
        Map<String,Object> parameters = new HashMap<String,Object>();
        if (feature.has(elementName)) {
            JsonNode properties = feature.get(elementName);

            Iterator<Map.Entry<String,JsonNode>> propertiesElements = properties.fields();
            while (propertiesElements.hasNext()) {
                Map.Entry<String,JsonNode> propertyElement = propertiesElements.next();
                JsonNode value = propertyElement.getValue();

                if (value.getNodeType().equals(JsonNodeType.NUMBER)) {
                    Double doubleValue = value.doubleValue();
                    parameters.put(propertyElement.getKey(), doubleValue);
                } else if (value.getNodeType().equals(JsonNodeType.STRING)) {
                    String stringValue = value.textValue();
                    parameters.put(propertyElement.getKey(), stringValue);
                } else if (value.getNodeType().equals(JsonNodeType.BOOLEAN)) {
                    Boolean booleanValue = value.booleanValue();
                    parameters.put(propertyElement.getKey(), booleanValue);
                } else {
                    parameters.put(propertyElement.getKey(), value);
                }
            }
        }
        return parameters;
    }

    protected void parseFeatureInfo(JsonNode feature) {
        if (feature.has("style")) this.styleJson = feature.get("style");
        this.parameters = parseParameters(feature, "properties");
    }

    public static Point jsonToPoint(JsonNode jsonPoint) {
        if (jsonPoint.isObject() && jsonPoint.has("x") && jsonPoint.has("y")) {
            Double x = jsonPoint.get("x").asDouble();
            Double y = jsonPoint.get("y").asDouble();
            return new Point(x,y);
        }
        return null;
    }

    public static Icon jsonStyleToIcon(JsonNode style) {
        Icon icon = new Icon();

        if (style==null) return icon;

        if (style.has("url")) {
            try {
                icon.setUrl(new URL(style.get("url").asText()));
            } catch (MalformedURLException e) {

            }
        }

        if (style.has("size")) {
            JsonNode jsonSize = style.get("size");
            Point size = jsonToPoint(jsonSize);
            if (size!=null) icon.setSize(size);
        }

        if (style.has("anchor")) {
            JsonNode jsonAnchor = style.get("anchor");
            Point anchor = jsonToPoint(jsonAnchor);
            if (anchor!=null) icon.setAnchor(anchor);
        }
        return icon;
    }

    public String getlayerName() {
        String name = "";
        if (this.parameters.containsKey("name")) {
            Object oName = this.parameters.get("name");
            if (oName instanceof String)
                name = (String)oName;
        }
        return name;
    }

    public static Path.Options jsonStyleToPathOptions(JsonNode style) {

        Path.Options options = new Path.Options(true);

        if (style==null) return options;

        if (style.has("stroke") && style.get("stroke").isBoolean()) {
            options.stroke = style.get("stroke").asBoolean();
        }

        if (style.has("fill") && style.get("fill").isBoolean()) {
            options.fill = style.get("fill").asBoolean();
        }

        if (style.has("color") && style.get("color").isTextual()) {
            options.color = style.get("color").asText();
        }

        if (style.has("fillColor") && style.get("fillColor").isTextual()) {
            options.fillColor = style.get("fillColor").asText();
        }

        if (style.has("lineCap") && style.get("lineCap").isTextual()) {
            options.lineCap = style.get("lineCap").asText();
        }

        if (style.has("lineJoin") && style.get("lineJoin").isTextual()) {
            options.lineCap = style.get("lineJoin").asText();
        }

        if (style.has("opacity") && style.get("opacity").isFloat()) {
            options.opacity = (float)style.get("opacity").asDouble();
        }

        if (style.has("fillOpacity") && style.get("fillOpacity").isFloat()) {
            options.fillOpacity = (float)style.get("fillOpacity").asDouble();
        }

        if (style.has("smoothFactor") && style.get("smoothFactor").isFloat()) {
            options.smoothFactor = (float)style.get("smoothFactor").asDouble();
        }

        if (style.has("weight") && style.get("weight").isInt()) {
            options.weight = style.get("weight").asInt();
        }

        return options;
    }

    public GeoJSON addData(JsonNode json) throws IOException {

        JsonNode features = json.isArray() ? json : json.get("features");
        if (features!=null && features.isArray()) {
            for (final JsonNode feature : features) {
                if (feature.has("geometries") || feature.has("geometry") || feature.has("features") || feature.has("coordinates")) {
                    this.addData(feature);
                }
            }
            return this;
        }

        boolean feature = json.has("type") ? json.get("type").asText().equals("Feature") : false;

        if (feature) {
            parseFeatureInfo(json);
        }
        String layerName = this.getlayerName();
        Layer layer = geometryToLayer(json,this.styleJson,this.getlayerName());
        if (layerName!=null)
            layer.setName(layerName);


        this.addLayer(layer);

        return this;
    }

    public static Layer geometryToLayer(JsonNode geojson) throws IOException {
        return geometryToLayer(geojson, null, "");
    }

    public static Layer geometryToLayer(JsonNode geojson, JsonNode style) throws IOException {
        return geometryToLayer(geojson, style, "");
    }

    public static Layer geometryToLayer(JsonNode geojson, JsonNode style, String name) throws IOException {

        boolean feature = geojson.has("type") ? geojson.get("type").asText().equals("Feature") : false;

        JsonNode geometry = feature ? geojson.get("geometry") : geojson;
        JsonNode coords = geometry.get("coordinates");

        String geometryType = geometry.get("type").asText();

        List<Layer> layers = new ArrayList<Layer>();

        if (geometryType.equals("Point")) {
             LatLng latLng = coordsToLatLng(coords);
             return new Marker(jsonStyleToIcon(style), latLng);
        } else if (geometryType.equals("MultiPoint")) {
             if (coords.isArray()) {
                 for (JsonNode coord : coords) {
                     LatLng latLng = coordsToLatLng(coord);
                     layers.add(new Marker(jsonStyleToIcon(style), latLng));
                 }
             }
        } else if (geometryType.equals("LineString") || geometryType.equals("MultiLineString")) {
            List<Ring<LatLng>> rings = coordsToRings(coords, geometryType.equals("LineString") ? 0 : 1);
            return new Polyline(rings).setOptions(jsonStyleToPathOptions(style));
        } else if (geometryType.equals("Polygon") || geometryType.equals("MultiPolygon"))  {
            List<Ring<LatLng>> rings = coordsToRings(coords, geometryType.equals("Polygon") ? 1 : 2);
            return new Polygon(rings).setOptions(jsonStyleToPathOptions(style));
        } else if (geometryType.equals("GeometryCollection")) {
            JsonNode geometries = geometry.get("geometries");
            for (JsonNode innerGeometry : geometries) {
                 layers.add(geometryToLayer(innerGeometry));
            }
        } else throw new IOException("Invalid GeoJSON object.");

        return new FeatureGroup(layers);
    }

    public static LatLng coordsToLatLng(JsonNode coords) {
        LatLng latLng = new LatLng(0,0);
        if (coords.isArray()) {
            ArrayNode jsonArray = (ArrayNode)coords;
            latLng.setLng(jsonArray.get(0).asDouble());
            latLng.setLat(jsonArray.get(1).asDouble());
        }
        return latLng;
    }

    public static List<LatLng> coordsToLatLngs(JsonNode coords) {
        List<LatLng> latlngs = new ArrayList<LatLng>();

        if (coords.isArray()) {
            for (JsonNode coord : coords) {
                LatLng latLng = coordsToLatLng(coord);
                latlngs.add(latLng);
            }
        }

        return latlngs;
    }

    public static List<Ring<LatLng>> coordsToRings(JsonNode coords, int levelsDeep) {
        List<Ring<LatLng>> rings = new ArrayList<Ring<LatLng>>();

        if (levelsDeep<=0) {
            List<LatLng> latLngs = coordsToLatLngs(coords);
            rings.add(new Ring<LatLng>(latLngs));
        } else if (coords.isArray()) {

            for (JsonNode coord : coords) {
                List<Ring<LatLng>> ringList = coordsToRings(coord, levelsDeep-1);
                rings.addAll(ringList);
            }

        }

        return rings;
    }
}
