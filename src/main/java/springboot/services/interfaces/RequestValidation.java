package springboot.services.interfaces;

import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.services.validation.request.interfaces.functional.ValidateRequestLogic;

public interface RequestValidation<RequestType>
{
	void validateRequest(RequestType aRequest, ValidationErrorContainer aListContainer, ValidateRequestLogic<RequestType> overRide);
}
