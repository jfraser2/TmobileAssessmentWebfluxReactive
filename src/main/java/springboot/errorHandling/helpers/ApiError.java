package springboot.errorHandling.helpers;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

//import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import springboot.enums.ZonedDateTimeEnum;

public class ApiError
{
	@JsonSerialize(using = HttpStatusConverter.class)
	private HttpStatus requestStatus;
	   
	@JsonSerialize(using = ZonedDateTimeConverter.class)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private ZonedDateTime timestamp;
	   
	private String message;
	private String debugMessage;
	@JsonSerialize(using = ListApiValidationErrorConverter.class)
	private List<ApiValidationError> subErrors;

	public ApiError() {
	    ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now();
		setTimestamp(zonedDateTime);
	}

	public ApiError(String zoneIdName) {
	    ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now(zoneIdName);
		setTimestamp(zonedDateTime);
	}
	
	public HttpStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(HttpStatus status) {
		this.requestStatus = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public List<ApiValidationError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<ApiValidationError> subErrors) {
		this.subErrors = subErrors;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	   
}
