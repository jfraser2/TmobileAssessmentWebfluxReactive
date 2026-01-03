package springboot.dto.validation.exceptions;

import java.util.List;

import springboot.errorHandling.helpers.ApiValidationError;

public class RequestValidationException
	extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3967697713578441774L;
	private List<ApiValidationError> subErrorList;
	
	public RequestValidationException(List<ApiValidationError> requestErrorList)
	{
		super();
		this.subErrorList = requestErrorList;
	}

	public List<ApiValidationError> getSubErrorList() {
		return this.subErrorList;
	}

}
