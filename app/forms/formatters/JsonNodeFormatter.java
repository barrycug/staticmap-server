package forms.formatters;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.format.Formatters;
import play.libs.Json;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by germanosin on 19.06.14.
 */
public class JsonNodeFormatter extends Formatters.SimpleFormatter<JsonNode> {


    @Override
    public JsonNode parse(String s, Locale locale) throws ParseException {
        JsonNode node = Json.parse(s);
        return node;
    }

    @Override
    public String print(JsonNode o, Locale locale) {
        return o.toString();
    }
}
