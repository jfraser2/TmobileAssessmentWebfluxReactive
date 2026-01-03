package springboot.controllers.rest;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.dto.request.CreateTask;
import springboot.dto.request.GetByStatus;
import springboot.dto.validation.exceptions.DatabaseRowNotFoundException;
import springboot.dto.validation.exceptions.EmptyListException;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.entities.TaskEntity;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.Task;
import springboot.services.validation.request.RequestValidationService;

@RestController
@RequestMapping(path="/rest/api")
public class TaskController
{
	@Autowired
	private Task taskService;
	
	@Autowired
	@Qualifier("requestValidationErrorsContainer")
	private ValidationErrorContainer requestValidationErrorsContainer;
	
	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.POST},
			path = "/v1/createTask",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> createTask(@RequestBody CreateTask data,
		ServerHttpRequest request,  @Parameter(hidden = true) @Autowired RequestValidationService<CreateTask> createTaskValidation)
		throws RequestValidationException, DatabaseRowNotFoundException, AccessDeniedException
	{
		
		// single field validation
		createTaskValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
		TaskEntity te = taskService.buildTaskEntity(data);
		TaskEntity savedEntity = taskService.persistData(te);
		
		String jsonString = goodResponse(savedEntity, requestStringBuilderContainer, null);
		te = null;
		savedEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		// 201 response
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.CREATED);
	}
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/all/tasks",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> allTasks(ServerHttpRequest request)
		throws EmptyListException, AccessDeniedException
	{
		
		List<TaskEntity> aList = taskService.findAll();
		boolean isEmpty = true;
		if(null != aList && aList.size() > 0) {
			isEmpty = false;
		}
		
		if(isEmpty) {
			throw new EmptyListException("Task Table is empty.", "TaskEntity");
		}
		
		List<Object> objectList = new ArrayList<Object>(aList);
		String jsonString = goodResponseList(objectList, requestStringBuilderContainer);
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/findByTaskStatus/{taskStatus}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> findByTaskStatus(@PathVariable(required = true) String taskStatus,
		ServerHttpRequest request, @Parameter(hidden = true) @Autowired RequestValidationService<GetByStatus> getByTaskStatusValidation)
		throws RequestValidationException, EmptyListException, AccessDeniedException
	{
		
		GetByStatus data = new GetByStatus(taskStatus);
		getByTaskStatusValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			throw new RequestValidationException(errorList);
		}
		
/*		
		List<TaskEntity> statusList = taskService.findByTaskStatus(taskStatus);
		boolean isEmpty = true;
		if(null != statusList && statusList.size() > 0) {
			isEmpty = false;
		}
		
		if(isEmpty) {
			throw new EmptyListException("No Task Table rows exist for Task Status: " + taskStatus, "TaskEntity");
		}
		
		List<Object> objectList = new ArrayList<Object>(statusList);
		String jsonString = goodResponseList(objectList, requestStringBuilderContainer);
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader(request);
*/		
		
		return taskService.findByTaskStatus(taskStatus);
	}
	
}
