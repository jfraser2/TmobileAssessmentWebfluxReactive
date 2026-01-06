package springboot.errorHandling.advices;

import java.nio.file.AccessDeniedException;

//import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import reactor.core.publisher.Mono;
import springboot.dto.validation.exceptions.DatabaseRowNotFoundException;
import springboot.dto.validation.exceptions.EmptyListException;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.enums.MapperEnum;
import springboot.errorHandling.helpers.ApiError;

/*
	One thing to keep in mind here is to match the exceptions declared with @ExceptionHandler with the exception used as the argument of the method.
	If these don’t match, the compiler will not complain – no reason it should, and Spring will not complain either.

	However, when the exception is actually thrown at runtime, the exception resolving mechanism will fail with:

	1 java.lang.IllegalStateException: No suitable resolver for argument [0] [type=...]
	2 HandlerMethod details: ...

*/

//  Advice execution precedence
//@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RequestValidationAdvice
{
	protected static final String UNEXPECTED_PROCESSING_ERROR = "{\"message\": \"Object could not convert to json\"}";


	//other exception handlers or handler overrides below
	
    @ExceptionHandler({ AccessDeniedException.class })
    public Mono<ResponseEntity<Object>> handleAccessDeniedException(
    		AccessDeniedException ex, ServerHttpRequest request)
    {
		ApiError apiError = new ApiError();
		apiError.setRequestStatus(HttpStatus.FORBIDDEN);
    	
 		String error = ex.getMessage();
        apiError.setMessage(error);
        
		String json = convertApiErrorToJson(apiError);
		apiError = null;
        
        return buildResponseEntity(json, HttpStatus.FORBIDDEN, request);
    }
	
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Object>> handleIllegalArgumentException(
    		IllegalArgumentException ex, ServerHttpRequest request)
    {
		ApiError apiError = new ApiError();
		apiError.setRequestStatus(HttpStatus.BAD_REQUEST);
		
        apiError.setMessage(ex.getMessage());
        
		String json = convertApiErrorToJson(apiError);
		apiError = null;
        
        return buildResponseEntity(json, HttpStatus.BAD_REQUEST, request);
    }
    
    @ExceptionHandler(DatabaseRowNotFoundException.class)
    public Mono<ResponseEntity<Object>> handleDatabaseRowNotFoundException(
    	DatabaseRowNotFoundException ex, ServerHttpRequest request)
    {
		ApiError apiError = new ApiError();
		apiError.setRequestStatus(HttpStatus.NOT_FOUND);
		
        apiError.setMessage(ex.getMessage());
        
		String json = convertApiErrorToJson(apiError);
		apiError = null;
        
        return buildResponseEntity(json, HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(EmptyListException.class)
    public Mono<ResponseEntity<Object>> handleEmptyListException(
    	EmptyListException ex, ServerHttpRequest request)
    {
    	String json = null;
        String rawJson = "{\"requestStatus\": \"OK\"," + "\"" + ex.getClassName() + "\": []}";
		try {
			ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();			
			if (null != rawJson)
			{
				JsonNode rootNode = mapper.readTree(rawJson);
				json = rootNode.toPrettyString();
			}
		}
		catch(JsonProcessingException jpe)
		{
			json = null;
		}
		
        return buildResponseEntity(json, HttpStatus.OK, request);
    }
    
    @ExceptionHandler(RequestValidationException.class)
    public Mono<ResponseEntity<Object>> handleRequestValidationException(
    	RequestValidationException ex, ServerHttpRequest request)
    {
		ApiError apiError = new ApiError();
		apiError.setRequestStatus(HttpStatus.BAD_REQUEST);
		
		String error = "Validation errors";
        apiError.setMessage(error);
        apiError.setSubErrors(ex.getSubErrorList());
   
		String json = convertApiErrorToJson(apiError);
		apiError = null;
        
        return buildResponseEntity(json, HttpStatus.BAD_REQUEST, request);
    }
	 
	private Mono<ResponseEntity<Object>> buildResponseEntity(String json, HttpStatus aStatus, ServerHttpRequest request)
	{
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		return Mono.just(new ResponseEntity<>(json, aResponseHeader, aStatus));
	}
	
	private String convertApiErrorToJson(ApiError apiError)
	{
		String json = null;
		try {
			ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
			
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(apiError);
		}
		catch(JsonProcessingException jpe)
		{
			json = UNEXPECTED_PROCESSING_ERROR;
		}
		
		return json;
	}
	
	private HttpHeaders createResponseHeader(ServerHttpRequest request)
	{
		// support CORS
//		System.out.println("Access-Control-Allow-Origin is: " + request.getHeader("Origin"));
		HttpHeaders aResponseHeader = new HttpHeaders();
		
		if (null != request) {
//			String tempOrigin = request.getHeader("Origin");
			String tempOrigin = request.getHeaders().getOrigin();			
			if (null != tempOrigin && tempOrigin.length() > 0) {
				aResponseHeader.add("Access-Control-Allow-Origin", tempOrigin);
			}	
//			aResponseHeader.add("Access-Control-Allow-Origin", "*");
		}
		aResponseHeader.add("Content-Type", "application/json");
		
		return aResponseHeader;
		
	}
	

}
