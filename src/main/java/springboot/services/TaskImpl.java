package springboot.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.dto.request.CreateTask;
import springboot.entities.TaskEntity;
import springboot.enums.ZonedDateTimeEnum;
import springboot.repositories.TaskRepository;
import springboot.services.interfaces.Task;

@Service
public class TaskImpl
    extends ServiceBase
	implements Task
{
	
	private static final String ENTITY_CLASS_NAME = "TaskEntity";
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Mono<ResponseEntity<Object>> findByTaskStatus(String taskStatus, ServerHttpRequest request, StringBuilderContainer requestStringBuilderContainer) {
		
		// support CORS - createResponseHeader(request);
		return taskRepository.findByTaskStatus(taskStatus)
			.collectList()         // Converts Flux<TaskEntity> to Mono<List<TaskEntity>>
			.<ResponseEntity<Object>>map(taskList ->
			    { List<Object> objectList = new ArrayList<Object>(taskList);
			      return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(goodResponseList(objectList, requestStringBuilderContainer));
			    })
			.defaultIfEmpty(ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(displayEmptyList(ENTITY_CLASS_NAME))); // Returns 200 and an empty json List if the Mono is empty
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Mono<ResponseEntity<Object>> findAll(ServerHttpRequest request, StringBuilderContainer requestStringBuilderContainer) {
		
		// support CORS - createResponseHeader(request);
		
		return taskRepository.findAll()
				.collectList()         // Converts Flux<TaskEntity> to Mono<List<TaskEntity>>
				.<ResponseEntity<Object>>map(taskList ->
				    { List<Object> objectList = new ArrayList<Object>(taskList);
				      return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(goodResponseList(objectList, requestStringBuilderContainer));
				    })
				.defaultIfEmpty(ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(displayEmptyList(ENTITY_CLASS_NAME))); // Returns 200 and an empty json List if the Mono is empty
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Mono<ResponseEntity<Object>> buildAndPersistTaskEntity(CreateTask createTaskRequest, ServerHttpRequest request, StringBuilderContainer requestStringBuilderContainer) {
		
		TaskEntity tempEntity = null;
		// support CORS - createResponseHeader(request);
		
		String taskName = createTaskRequest.getTaskName();
		String taskStatus = createTaskRequest.getTaskStatus();
		
		tempEntity = new TaskEntity();
		tempEntity.setTaskName(taskName);
		tempEntity.setTaskDescription(createTaskRequest.getTaskDescription());
		tempEntity.setTaskStatus(taskStatus);
	    ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now();
		tempEntity.setTaskCreateDate(zonedDateTime);
		
		Mono<TaskEntity> tempMono = Mono.just(tempEntity);
		
		// flatMap is designed for asynchronous, one-to-many transformations
		// map is designed for synchronous, one-to-one data transformations 
		
		return  tempMono.flatMap(task -> taskRepository.save(task))
				.<ResponseEntity<Object>>map(savedEntity ->
				    { return ResponseEntity.status(HttpStatus.CREATED).headers(createResponseHeader(request)).body(goodResponse(savedEntity, requestStringBuilderContainer, null));
				});
	}

}
