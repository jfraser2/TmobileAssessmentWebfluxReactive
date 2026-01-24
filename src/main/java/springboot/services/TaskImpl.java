package springboot.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Mono;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.dto.request.CreateTask;
import springboot.dto.validation.exceptions.DatabaseRowNotFoundException;
import springboot.entities.TaskEntity;
import springboot.enums.ZonedDateTimeEnum;
import springboot.repositories.TaskRepository;
import springboot.services.interfaces.Task;

// helpful article
// https://www.google.com/search?q=reactvie+transaction+Manager+and+r2dbc+repository+example+java&rlz=1C1JSBI_enUS1092US1092&oq=reactvie+transaction+Manager+and+r2dbc+repository+example+java&gs_lcrp=EgZjaHJvbWUyBggAEEUYOTIHCAEQIRiPAjIHCAIQIRiPAtIBCjQ4ODY0ajBqMTWoAgiwAgHxBVuDNjJhmoNx8QVbgzYyYZqDcQ&sourceid=chrome&ie=UTF-8

// Alternatively, for simple operations, the @Transactional annotation works
// if the method returns a reactive type (Mono or Flux)

// Warning Message
// See http://www.h2database.com/html/features.html#read_only

@Service
public class TaskImpl
    extends ServiceBase
	implements Task, ApplicationContextAware
{
	
	private static final String ENTITY_CLASS_NAME = "TaskEntity";
	private static final String NOT_FOUND_TABLE_NAME = "Task";
	
	@Autowired
	private TaskRepository taskRepository;
	
	private ApplicationContext applicationContext;
	
	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }	
	
	@Override
	public Mono<ResponseEntity<Object>> findByTaskStatus(String taskStatus, ServerHttpRequest request)
	{
		
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) this.applicationContext.getBean("requestStringBuilderContainer");
		TransactionalOperator readOnlyTransactionalOperator = 
				(TransactionalOperator) this.applicationContext.getBean("readOnlyTransactionalOperator");		
		
		// support CORS - createResponseHeader(request);
		// map is designed for synchronous, one-to-one data transformations
		
		return taskRepository.findByTaskStatus(taskStatus)
			.collectList()         // Converts Flux<TaskEntity> to Mono<List<TaskEntity>>
			.<ResponseEntity<Object>>map(taskList -> {
			    if (null != taskList && taskList.size() > 0) {
			      List<Object> objectList = new ArrayList<Object>(taskList);
			      return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(goodResponseList(objectList, requestStringBuilderContainer));
			    } else {
				  return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(displayEmptyList(ENTITY_CLASS_NAME)); // Returns 200 and an empty json List if the Mono is empty 
			    }
			})
			.as(readOnlyTransactionalOperator::transactional); // Wrap the operations in a transaction
	}

	@Override
	public Mono<ResponseEntity<Object>> findAll(ServerHttpRequest request)
	{
		
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) this.applicationContext.getBean("requestStringBuilderContainer");
		TransactionalOperator readOnlyTransactionalOperator = 
				(TransactionalOperator) this.applicationContext.getBean("readOnlyTransactionalOperator");
		
		// support CORS - createResponseHeader(request);
		// map is designed for synchronous, one-to-one data transformations
		
		return taskRepository.findAll()
			.collectList()         // Converts Flux<TaskEntity> to Mono<List<TaskEntity>>
			.<ResponseEntity<Object>>map(taskList -> {
				if (null != taskList && taskList.size() > 0) {
				  List<Object> objectList = new ArrayList<Object>(taskList);
				  return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(goodResponseList(objectList, requestStringBuilderContainer));
				} else {
				  return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(displayEmptyList(ENTITY_CLASS_NAME)); // Returns 200 and an empty json List if the Mono is empty 
				}
			})
			.as(readOnlyTransactionalOperator::transactional); // Wrap the operations in a transaction
	}
	

	@Override
	public Mono<ResponseEntity<Object>> buildAndPersistTaskEntity(CreateTask createTaskRequest, ServerHttpRequest request)
	{
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) this.applicationContext.getBean("requestStringBuilderContainer");
		TransactionalOperator transactionalOperator = 
				(TransactionalOperator) this.applicationContext.getBean("transactionalOperator");
		
		TaskEntity tempEntity = null;
		
		String taskName = createTaskRequest.getTaskName();
		String taskStatus = createTaskRequest.getTaskStatus();
		
		tempEntity = new TaskEntity();
		tempEntity.setTaskName(taskName);
		tempEntity.setTaskDescription(createTaskRequest.getTaskDescription());
		tempEntity.setTaskStatus(taskStatus);
	    ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now();
		tempEntity.setTaskCreateDate(zonedDateTime);
		tempEntity.setTaskLastUpdateDate(null);
		
		Mono<TaskEntity> tempMono = Mono.just(tempEntity);
		
		// support CORS - createResponseHeader(request);
		// flatMap is designed for asynchronous, one-to-many transformations
		// map is designed for synchronous, one-to-one data transformations 
		
		return  tempMono.flatMap(task -> taskRepository.save(task))
				.<ResponseEntity<Object>>map(savedEntity -> {
					// In the future write entityToJson to a Kafka queue.
					// Another process(maybe mulesoft) can read the queue and store the json,
					// in an Iceberg table living in AWS S3.
					// The S3 bucket will store the json, using the OLAP data lake format parquet.
					// Then snowflake can use it.
					String entityToJson = goodResponse(savedEntity, requestStringBuilderContainer, null);
				    return ResponseEntity.status(HttpStatus.CREATED).headers(createResponseHeader(request)).body(entityToJson);
				})
				.as(transactionalOperator::transactional); // Wrap the operations in a transaction
	}

	
	@Override
	public Mono<ResponseEntity<Object>> findByTaskId(Long id, ServerHttpRequest request)
	{
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) this.applicationContext.getBean("requestStringBuilderContainer");
		TransactionalOperator readOnlyTransactionalOperator = 
				(TransactionalOperator) this.applicationContext.getBean("readOnlyTransactionalOperator");
		
		// support CORS - createResponseHeader(request);
		// map is designed for synchronous, one-to-one data transformations 
		
		return taskRepository.findById(id)
	            .switchIfEmpty(Mono.error(new DatabaseRowNotFoundException(buildNoDatabaseRowMessage(NOT_FOUND_TABLE_NAME, id))))
	            .<ResponseEntity<Object>>map(task -> {
					return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(goodResponse(task, requestStringBuilderContainer, null));
				})
				.as(readOnlyTransactionalOperator::transactional); // Wrap the operations in a transaction
	}
	
/*
    public Mono<User> updateUserNameInTransaction(Long id, String newName) {
        // The entire chain within the .as(operator::transactional) block runs in a single transaction
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
            .flatMap(user -> {
                user.setName(newName);
                // Additional operations can be chained here, e.g., saving related entities
                return userRepository.save(user);
            })
            .as(transactionalOperator::transactional); // Wrap the operations in a transaction
    }
*/    
    
}
