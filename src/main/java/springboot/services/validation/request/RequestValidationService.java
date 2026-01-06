package springboot.services.validation.request;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.services.interfaces.RequestValidation;
import springboot.services.validation.request.interfaces.functional.ValidateRequestLogic;

// uses java generics
@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS) // LifeCycle Ends at Instantiation because of prototype
public class RequestValidationService<RequestType>
	extends RequestValidationDefaultMethods<RequestType>
	implements RequestValidation<RequestType>
{
	
	public RequestValidationService()
	{
	}
	
	public void validateRequest(RequestType aRequest, ValidationErrorContainer aListContainer, ValidateRequestLogic<RequestType> overRide)
	{
		ValidateRequestLogic<RequestType> methodToExecute = null;
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = getDefaultValidateRequest();
		}
		
		methodToExecute.validateRequest(aRequest, aListContainer);
		
		return;
	}
	
}
