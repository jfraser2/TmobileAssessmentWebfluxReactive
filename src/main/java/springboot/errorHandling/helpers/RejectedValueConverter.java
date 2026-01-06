package springboot.errorHandling.helpers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class RejectedValueConverter
	extends JsonSerializer<Object>
{

	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
			throws IOException
	{
		jgen.writeString(value.toString());
	}

}
