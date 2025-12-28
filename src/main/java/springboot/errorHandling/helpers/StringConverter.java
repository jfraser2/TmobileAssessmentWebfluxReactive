package springboot.errorHandling.helpers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StringConverter
	extends JsonSerializer<String>
{
	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider serializers)
			throws IOException
	{
		jgen.writeString(value);
	}

}
