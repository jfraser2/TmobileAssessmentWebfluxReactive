package springboot.errorHandling.helpers;

import java.io.IOException;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import springboot.enums.ZonedDateTimeEnum;

public class ZonedDateTimeConverter
	extends JsonSerializer<ZonedDateTime>
{

	@Override
	public void serialize(ZonedDateTime value, JsonGenerator jgen, SerializerProvider serializers)
		throws IOException
	{
		if (null != value)
		{
			String zonedDateTimeAsString = ZonedDateTimeEnum.INSTANCE.writeDateString(value, ZonedDateTimeEnum.INSTANCE.DATE_FORMAT3);
            
			jgen.writeString(zonedDateTimeAsString);
		}
	}

}
