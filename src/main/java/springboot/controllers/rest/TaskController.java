package springboot.controllers.rest;

import java.nio.file.AccessDeniedException;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.Task;
import springboot.services.validation.request.RequestValidationService;

@RestController
@RequestMapping(path="/rest/api")
public class TaskController
	implements ApplicationContextAware 
{
	private ApplicationContext applicationContext;
	
	@Autowired
	private Task taskService;
	
	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
	
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
		
		StringBuilderContainer requestStringBuilderContainer = 
			(StringBuilderContainer) this.applicationContext.getBean("requestStringBuilderContainer");
		ValidationErrorContainer requestValidationErrorsContainer = 
			(ValidationErrorContainer) this.applicationContext.getBean("requestValidationErrorsContainer");		
		
		// single field validation
		createTaskValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			Mono.error(new RequestValidationException(errorList));
		}
		
		// 201 response
		return taskService.buildAndPersistTaskEntity(data, request, requestStringBuilderContainer);
	}
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/all/tasks",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> allTasks(ServerHttpRequest request)
		throws AccessDeniedException
	{
		StringBuilderContainer requestStringBuilderContainer = 
			(StringBuilderContainer) this.applicationContext.getBean("requestStringBuilderContainer");
		
		return taskService.findAll(request, requestStringBuilderContainer);
		
	}
	
	// Mono controller method will automatically subscribe()
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/findByTaskStatus/{taskStatus}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<Object>> findByTaskStatus(@PathVariable(required = true) String taskStatus,
		ServerHttpRequest request, @Parameter(hidden = true) @Autowired RequestValidationService<GetByStatus> getByTaskStatusValidation)
		throws RequestValidationException, AccessDeniedException
	{
		
		StringBuilderContainer requestStringBuilderContainer = 
			(StringBuilderContainer) this.applicationContext.getBean("requestStringBuilderContainer");
		ValidationErrorContainer requestValidationErrorsContainer = 
				(ValidationErrorContainer) this.applicationContext.getBean("requestValidationErrorsContainer");		
		
		GetByStatus data = new GetByStatus(taskStatus);
		getByTaskStatusValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			Mono.error(new RequestValidationException(errorList));			
		}
		
		
		return taskService.findByTaskStatus(taskStatus, request, requestStringBuilderContainer);
	}
	
}
