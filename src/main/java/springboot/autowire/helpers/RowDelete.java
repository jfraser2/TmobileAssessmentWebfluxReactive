package springboot.autowire.helpers;

import java.time.ZonedDateTime;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import springboot.errorHandling.helpers.ZonedDateTimeConverter;

public class RowDelete {
	
	@JsonSerialize(using = ZonedDateTimeConverter.class)
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime  timestamp;

	private String message;
	
	public RowDelete() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
