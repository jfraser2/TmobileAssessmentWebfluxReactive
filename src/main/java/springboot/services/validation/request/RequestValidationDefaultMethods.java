package springboot.services.validation.request;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import springboot.services.validation.request.interfaces.functional.ValidateRequestLogic;

//uses java generics
public abstract class RequestValidationDefaultMethods<RequestType>
{
	protected final ValidateRequestLogic<RequestType> defaultValidateRequest = (aRequest, aListContainer) ->
	{
		// Since it is AutoWired clear the List before you use it
		aListContainer.clearValidationErrors();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<RequestType>> violations = validator.validate(aRequest);
		Path propertyPath;
		String fieldName = "";
		String objectName = aRequest.getClass().getSimpleName();
		String message;
		Object invalidValue;
		
		for(ConstraintViolation<RequestType> aViolation : violations)
		{
			invalidValue = aViolation.getInvalidValue();
			message = aViolation.getMessage();
			propertyPath = aViolation.getPropertyPath();
			for (Path.Node aNode : propertyPath)
			{
				fieldName = aNode.getName();
			}
			aListContainer.addToList(objectName, fieldName, invalidValue, message);
		}
		
		return;
	};
	
	protected ValidateRequestLogic<RequestType> getDefaultValidateRequest()
	{
		return this.defaultValidateRequest;
	}

}
