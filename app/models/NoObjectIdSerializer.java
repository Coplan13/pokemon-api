package models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by jeromeheissler on 27/02/2017.
 */
public class NoObjectIdSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if(value == null){
            jgen.writeNull();
        }else{
            jgen.writeString(value);
        }
    }

}
