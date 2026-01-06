package springboot.services.validation.request.interfaces.functional;

import springboot.autowire.helpers.ValidationErrorContainer;

public interface ValidateRequestLogic<RequestType>
{
	void validateRequest(RequestType aRequest, ValidationErrorContainer aListContainer);
}
