package springboot.errorHandling.helpers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateConverter
	extends JsonSerializer<Date>

{
	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider serializers)
			throws IOException
	{
		if (null != value)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			String dateAsString = sdf.format(value);
			jgen.writeString(dateAsString);
		}
		
	}

}
