package springboot.errorHandling.helpers;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class HttpStatusConverter
	extends JsonSerializer<HttpStatus>
{

	@Override
	public void serialize(HttpStatus value, JsonGenerator jgen, SerializerProvider serializers)
		throws IOException
	{
		if (null != value)
		{
			jgen.writeString(value.getReasonPhrase());
		}
	}

}
