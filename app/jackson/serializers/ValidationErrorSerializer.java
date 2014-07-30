package jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import play.data.validation.ValidationError;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 14:08
 */
public class ValidationErrorSerializer extends JsonSerializer<ValidationError> {
    @Override
    public void serialize(ValidationError value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        jgen.writeStartObject();
        jgen.writeStringField("key", value.key());

        jgen.writeArrayFieldStart("messages");
        for (String message : value.messages()) {
            jgen.writeString(message);
        }
        jgen.writeEndArray();

        jgen.writeEndObject();
    }
}
