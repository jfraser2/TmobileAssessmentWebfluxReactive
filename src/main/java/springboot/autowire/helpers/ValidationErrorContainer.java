package springboot.autowire.helpers;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PreDestroy;

import springboot.errorHandling.helpers.ApiValidationError;

public class ValidationErrorContainer {
	
	private List<ApiValidationError> errorList;
	
	public  ValidationErrorContainer()
	{
		this.errorList = new ArrayList<ApiValidationError>();
	}

	// Since it is AutoWired clear the errorList before you use it
	public void clearValidationErrors()
	{
		// clear the errorList
		this.errorList.clear();
	}
	
	public List<ApiValidationError> getValidationErrorList() {
		return this.errorList;
	}

	public int size()
	{
		return this.errorList.size();
	}
	
	public void addToList(String objectName, String fieldName, Object invalidValue, String message)
	{
		ApiValidationError validationError = new ApiValidationError(objectName, fieldName, invalidValue, message);
		this.errorList.add(validationError);
	}
	
    @PreDestroy
    public void onDestroy() {
    	// give Memory Back to the JVM, when the Request is over
    	clearValidationErrors();
    	this.errorList = null;
        System.out.println("ValidationError Container is destroyed!!!");
    }    

}
