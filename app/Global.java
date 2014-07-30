import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import factory.BaseMapFactory;
import forms.MapSize;
import forms.formatters.JsonNodeFormatter;
import forms.formatters.LatLngFormatter;
import forms.formatters.MapSizeFormatter;
import jackson.serializers.ValidationErrorSerializer;
import org.gradoservice.mapRender.geo.LatLng;
import org.gradoservice.mapRender.layers.OSM;
import org.gradoservice.mapRender.layers.Rekod;
import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;
import play.data.validation.ValidationError;
import play.libs.Json;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 18:50
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {

        // register custom serializer for ValidationError class
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule("SimpleModule",
                new Version(1,0,0,null));
        simpleModule.addSerializer(ValidationError.class, new ValidationErrorSerializer());
        mapper.registerModule(simpleModule);
        Json.setObjectMapper(mapper);

        // Register Custom formaters for Forms
        Formatters.register(LatLng.class, new LatLngFormatter());
        Formatters.register(MapSize.class, new MapSizeFormatter());
        Formatters.register(JsonNode.class, new JsonNodeFormatter());

        // Register BaseMaps classes
        BaseMapFactory.registerBaseMap("rekod", Rekod.class);
        BaseMapFactory.registerBaseMap("osm", OSM.Mapnik.class);
    }
}
