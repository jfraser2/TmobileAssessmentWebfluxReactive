package springboot.errorHandling.helpers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ApiValidationError
	extends ApiSubError
{
	@JsonSerialize(using = StringConverter.class)
	private String object;
	@JsonSerialize(using = StringConverter.class)
	private String field;
	@JsonSerialize(using = RejectedValueConverter.class)
	private Object rejectedValue;
	@JsonSerialize(using = StringConverter.class)
	private String message;

	public ApiValidationError(String object, String message) {
	    this.object = object;
	    this.message = message;
	}
	
	public ApiValidationError(String objectName, String fieldName, Object anInvalidValue, String aMessage)
	{
		if (null != objectName)
		{
			this.object = objectName;
		}
		else
		{
			this.object = "";
		}
		if (null != fieldName)
		{
			this.field = fieldName;
		}
		else
		{
			this.field = "";
		}
		if (null != anInvalidValue)
		{
			this.rejectedValue = anInvalidValue;
		}
		else
		{
			this.rejectedValue = "";
		}
		if (null != aMessage)
		{
			this.message = aMessage;
		}
		else
		{
			this.message = "";
		}
	}

}
