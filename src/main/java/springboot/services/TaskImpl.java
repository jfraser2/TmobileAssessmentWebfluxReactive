package springboot.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springboot.autowire.helpers.RowDelete;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.dto.request.CreateTask;
import springboot.dto.request.UpdateTaskStatus;
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
	implements Task
{
	
	private static final String ENTITY_CLASS_NAME = "TaskEntity";
	private static final String NOT_FOUND_TABLE_NAME = "Task";
	
	@Autowired
	private TaskRepository taskRepository;
	
	private final TransactionLogicService transactionLogicService; // Inject the new service

    @Autowired
    public TaskImpl(TransactionLogicService transactionLogicService) {
    	this.transactionLogicService = transactionLogicService;
    }
	
	@Override
	public Mono<ResponseEntity<Object>> findByTaskStatus(String taskStatus, ServerHttpRequest request)
	{
		
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) getBean(STRING_BUILDER_CONTAINER);
		TransactionalOperator readOnlyTransactionalOperator = 
				(TransactionalOperator) getBean(READ_ONLY_TRANSACTIONAL_OPERATOR);		
		
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
				(StringBuilderContainer) getBean(STRING_BUILDER_CONTAINER);
		TransactionalOperator readOnlyTransactionalOperator = 
				(TransactionalOperator) getBean(READ_ONLY_TRANSACTIONAL_OPERATOR);
		
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
				(StringBuilderContainer) getBean(STRING_BUILDER_CONTAINER);
		TransactionalOperator transactionalOperator = 
				(TransactionalOperator) getBean(TRANSACTIONAL_OPERATOR);
		
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
		
		Mono<ResponseEntity<Object>> retVar = null;
		
		boolean databaseSaveFailed = true;

		try {
			Flux<ResponseEntity<Object>> tempFlux = transactionLogicService.createTransactionResult(request,
				tempEntity, transactionalOperator, requestStringBuilderContainer);
			retVar = tempFlux.next();  // returns a Mono of the Flux element
		} catch (TransactionException te) {
			String errorJson = buildDatabaseOrQueueingError("A database insert Transaction failed: " + te.getMessage());
			retVar = Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson));
		} catch (RuntimeException re) {
			String errorJson = buildDatabaseOrQueueingError("A database insert Transaction failed: " + re.getMessage());
			retVar = Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson));
		}
		
		return retVar; 
		
		
				
/*		
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
*/				
	}

	
	@Override
	public Mono<ResponseEntity<Object>> findByTaskId(Long id, ServerHttpRequest request)
	{
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) getBean(STRING_BUILDER_CONTAINER);
		TransactionalOperator readOnlyTransactionalOperator = 
				(TransactionalOperator) getBean(READ_ONLY_TRANSACTIONAL_OPERATOR);
		
		// support CORS - createResponseHeader(request);
		// map is designed for synchronous, one-to-one data transformations 
		
		return taskRepository.findById(id)
	            .switchIfEmpty(Mono.error(new DatabaseRowNotFoundException(buildNoDatabaseRowMessage(NOT_FOUND_TABLE_NAME, id))))
	            .<ResponseEntity<Object>>map(task -> {
					return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(goodResponse(task, requestStringBuilderContainer, null));
				})
				.as(readOnlyTransactionalOperator::transactional); // Wrap the operations in a transaction
	}
	
	@Override
	public Mono<ResponseEntity<Object>> updateTaskStatus(UpdateTaskStatus updateTaskStatus, ServerHttpRequest request) {
        // The entire chain within the .as(operator::transactional) block runs in a single transaction
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) getBean(STRING_BUILDER_CONTAINER);
		TransactionalOperator transactionalOperator = 
				(TransactionalOperator) getBean(TRANSACTIONAL_OPERATOR);
		TransactionalOperator readOnlyTransactionalOperator = 
				(TransactionalOperator) getBean(READ_ONLY_TRANSACTIONAL_OPERATOR);
		
		try {
			return transactionLogicService.preReadTaskById(
					updateTaskStatus.getId(), readOnlyTransactionalOperator)
			.<ResponseEntity<Object>>flatMap(fetchedTask -> {
				
				if (fetchedTask.getId() != -1L) {
					ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now();
					fetchedTask.setTaskLastUpdateDate(zonedDateTime);
					fetchedTask.setTaskStatus(updateTaskStatus.getNewTaskStatus());
					
					Flux<ResponseEntity<Object>> tempFlux = transactionLogicService.updateTransactionResult(request,
							fetchedTask, transactionalOperator, requestStringBuilderContainer);	
					return tempFlux.next();
					
				} else {
					return Mono.error(new DatabaseRowNotFoundException(buildNoDatabaseRowMessage(NOT_FOUND_TABLE_NAME, updateTaskStatus.getId())));
				}
			}); // end the flatMap
		} catch (TransactionException te) {
			String errorJson = buildDatabaseOrQueueingError("A database updateTransaction failed: " + te.getMessage());
			return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson));
		} catch (RuntimeException re) {
			String errorJson = buildDatabaseOrQueueingError("A database update Transaction failed: " + re.getMessage());
			return  Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson));
		}
		
		

/*
	
        return taskRepository.findById(updateTaskStatus.getId())
            .switchIfEmpty(Mono.error(new DatabaseRowNotFoundException(buildNoDatabaseRowMessage(NOT_FOUND_TABLE_NAME, updateTaskStatus.getId()))))
            .flatMap(fetchedTask -> {
        	    ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now();
        		fetchedTask.setTaskLastUpdateDate(zonedDateTime);
                fetchedTask.setTaskStatus(updateTaskStatus.getNewTaskStatus());
                return taskRepository.save(fetchedTask);                
            })
            .<ResponseEntity<Object>>map(savedEntity -> {
                
                // Additional operations can be chained here, e.g., saving related entities
               	String entityToJson = goodResponse(savedEntity, requestStringBuilderContainer, null);
               	return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(entityToJson);
            })
            .as(transactionalOperator::transactional); // Wrap the operations in a transaction
*/            
           
    }
    
	@Override
	public Mono<ResponseEntity<Object>> deleteTask(Long taskId, ServerHttpRequest request) {
        // The entire chain within the .as(operator::transactional) block runs in a single transaction
		StringBuilderContainer requestStringBuilderContainer = 
				(StringBuilderContainer) getBean(STRING_BUILDER_CONTAINER);
		RowDelete rowDelete = (RowDelete) getBean(ROW_DELETE_BEAN);
		TransactionalOperator transactionalOperator = 
				(TransactionalOperator) getBean(TRANSACTIONAL_OPERATOR);
		
    	rowDelete.setTimestamp(ZonedDateTimeEnum.INSTANCE.now());
    	String message = buildRowDeleteMessage(NOT_FOUND_TABLE_NAME, taskId);
    	rowDelete.setMessage(message);
    	
		// support CORS - createResponseHeader(request);
		// flatMap is designed for asynchronous, one-to-many transformations
		// map is designed for synchronous, one-to-one data transformations 
    	
        return taskRepository.findById(taskId)
            .switchIfEmpty(Mono.error(new DatabaseRowNotFoundException(buildNoDatabaseRowMessage(NOT_FOUND_TABLE_NAME, taskId))))
            .flatMap(fetchedTask -> taskRepository.delete(fetchedTask)
            		.then(Mono.just(fetchedTask)) // 3. Return a Mono of the original object            		
            )
            .<ResponseEntity<Object>>map(fetchedEntity -> {
            	
               	String entityToJson = goodResponse(rowDelete, requestStringBuilderContainer, null);
                
                // Additional operations can be chained here, e.g., saving related entities
               	return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(entityToJson);
            })
            .as(transactionalOperator::transactional); // Wrap the operations in a transaction
    }
    
}
