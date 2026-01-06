package springboot.errorHandling.helpers;

import com.google.gson.*;

import springboot.enums.ZonedDateTimeEnum;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeAdapter
	implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime>
{
	private final DateTimeFormatter FORMATTER;
	
	public ZonedDateTimeAdapter() {
		// default ZoneIf = 
		FORMATTER = DateTimeFormatter.ofPattern(ZonedDateTimeEnum.INSTANCE.DATE_FORMAT3)
				.withZone(ZonedDateTimeEnum.INSTANCE.getZoneId());
	}
	
    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(src));
    }

    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    	throws JsonParseException
    {
        return ZonedDateTime.parse(json.getAsString(), FORMATTER);
    }
    
}
