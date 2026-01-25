package springboot.controllers.rest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.annotations.ApiImplicitParam;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.dto.request.CreateTask;
import springboot.dto.request.GetById;
import springboot.dto.request.GetByStatus;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.Task;
import springboot.services.validation.request.RequestValidationService;

@RestController
@RequestMapping(path="/rest/api")
public class TaskController
	extends ControllerBase
{
	
	@Autowired
	private Task taskService;
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.POST},
			path = "/v1/createTask",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> createTask(@RequestBody CreateTask data, ServerHttpRequest request, 
		@Parameter(hidden = true) @Autowired RequestValidationService<CreateTask> createTaskValidation)
		throws RequestValidationException, AccessDeniedException
	{
		
		ValidationErrorContainer requestValidationErrorsContainer = 
			(ValidationErrorContainer) getBean(VALIDATION_ERRORS_CONTAINER);		
		
		// single field validation
		createTaskValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("createTask - Right before the throw");
			return Mono.error(new RequestValidationException(errorList));			
		}
		
		// 201 response
		return taskService.buildAndPersistTaskEntity(data, request);
	}
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/all/tasks",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> allTasks(ServerHttpRequest request)
		throws AccessDeniedException
	{
		return taskService.findAll(request);
		
	}
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/findByTaskStatus/{taskStatus}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> findByTaskStatus(@PathVariable(required = true) String taskStatus, ServerHttpRequest request,
		@Parameter(hidden = true) @Autowired RequestValidationService<GetByStatus> getByTaskStatusValidation)
		throws RequestValidationException, AccessDeniedException
	{
		
		ValidationErrorContainer requestValidationErrorsContainer = 
				(ValidationErrorContainer) getBean(VALIDATION_ERRORS_CONTAINER);		
		
		GetByStatus data = new GetByStatus(URLDecoder.decode(taskStatus, StandardCharsets.UTF_8));
		getByTaskStatusValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("findByStatus - Right before the throw passed");
			return Mono.error(new RequestValidationException(errorList));			
		}
		
		
		return taskService.findByTaskStatus(data.getTaskStatus(), request);
	}
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/findByTaskId/{taskId}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> findByTaskId(@PathVariable(required = true) String taskId, ServerHttpRequest request,
		@Parameter(hidden = true) @Autowired RequestValidationService<GetById> getByTaskIdValidation)
		throws RequestValidationException, AccessDeniedException
	{
		
		ValidationErrorContainer requestValidationErrorsContainer = 
				(ValidationErrorContainer) getBean(VALIDATION_ERRORS_CONTAINER);		
		
		GetById data = new GetById(URLDecoder.decode(taskId, StandardCharsets.UTF_8));
		getByTaskIdValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("findByTaskId - Right before the throw passed");
			return Mono.error(new RequestValidationException(errorList));			
		}
		
		Long tempLong = Long.valueOf(data.getId());
		return taskService.findByTaskId(tempLong, request);
	}
	
}
