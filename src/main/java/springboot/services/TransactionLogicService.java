package springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Mono;
import springboot.autowire.helpers.RowDelete;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.dto.processing.QueueResult;
import springboot.dto.response.NonModelAdditionalFields;
import springboot.entities.TaskEntity;
import springboot.enums.OperationEnum;
import springboot.enums.ZonedDateTimeEnum;
//import springboot.enums.ZonedDateTimeEnum;
import springboot.repositories.TaskRepository;

@Service
public class TransactionLogicService
	extends ServiceBase
{
	
	@Autowired
	private TaskRepository taskRepository;

/*	
	private TaskEntity buildFake(boolean doingUpdate) {
		
        TaskEntity fake = new TaskEntity();
		fake.setTaskName("Dummy");
		fake.setTaskDescription("Dummy description");
		fake.setTaskStatus("Assigned");
	    ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now();
		if (doingUpdate) {
			fake.setTaskCreateDate(zonedDateTime);
			fake.setTaskLastUpdateDate(zonedDateTime);
		} else {
			fake.setTaskCreateDate(zonedDateTime);
			fake.setTaskLastUpdateDate(null);
		}	
		return fake;
	}
*/	

	public Mono<TaskEntity> preReadTaskById(Long recordId, TransactionalOperator transactionalOperator) {
		
		return transactionalOperator.execute(status -> {  

			// Perform updates/inserts within this block
				
			// support CORS - createResponseHeader(request);
			// flatMap is designed for asynchronous, one-to-many transformations
			// map is designed for synchronous, one-to-one data transformations
			
			// .map() automatically converts the return Object to a Mono 
			// .flatMap() does not
			
			return taskRepository.findById(recordId)
//		    .switchIfEmpty(Mono.error(new DatabaseRowNotFoundException(buildNoDatabaseRowMessage(NOT_FOUND_TABLE_NAME, recordId))))
			.defaultIfEmpty(new TaskEntity(-1L))					
	        .doOnError(ex -> {
	            // This block is executed if an error is thrown within the transaction
	            System.out.println(" preReadTaskById Transaction failed: " + ex.getMessage());
	            // The transaction will be marked for rollback automatically
	            if (!status.isRollbackOnly()) {
	            	status.setRollbackOnly();
	            }
	        }) // end doOnError	
			.<TaskEntity>map(fetchedEntity -> {
//				System.out.println("Chain did Continue");
				if (!status.isRollbackOnly()) {
					return fetchedEntity;
				} else {
					TaskEntity nullEntity = new TaskEntity(-1L);
					return nullEntity;
				}
	        }) ; // end the map automatically converts the return Object to a Mono
		}).last(); // end the execute
	}
	
	public Mono<ResponseEntity<Object>> createTransactionResult( ServerHttpRequest request, 
			TaskEntity task,
			TransactionalOperator transactionalOperator,
			StringBuilderContainer requestStringBuilderContainer)
	{
		
		// Wrap the operations in a transaction	
		// Using the Lambda Implementation of the Callback doInTransaction(ReactiveTransaction status) method 
		return transactionalOperator.execute(status -> {  

			// Perform updates/inserts within this block
				
			// support CORS - createResponseHeader(request);
			// flatMap is designed for asynchronous, one-to-many transformations
			// map is designed for synchronous, one-to-one data transformations
			
			// .map() automatically converts the return Object to a Mono 
			// .flatMap() does not
			
			return taskRepository.save(task)
	        .doOnError(ex -> {
	            // This block is executed if an error is thrown within the transaction
	            System.out.println("Transaction failed: " + ex.getMessage());
	            // The transaction will be marked for rollback automatically
	            if (!status.isRollbackOnly()) {
	            	status.setRollbackOnly();
	            }
	        }) // end doOnError	        
			.<ResponseEntity<Object>>flatMap(savedEntity -> {  
	        	// In the future write entityToJson to Kafka or RabbitMQ.
				// Another process(maybe mulesoft or AWS Lambda or Apache Flink) can read the queue
				//  and store the json, in an Iceberg table living in AWS S3.
				// The S3 bucket would store the json, using the OLAP data lake format parquet.
				// Then snowflake can use it.
				QueueResult result = new QueueResult(savedEntity, false);
	            	
				String errorJson = null;
				if (!status.isRollbackOnly()) { // check if the database insert worked
					NonModelAdditionalFields additionalFields = new NonModelAdditionalFields(
						"T-Mobile", OperationEnum.CREATE.getValue());
					String queueJson = goodResponse(savedEntity, requestStringBuilderContainer, additionalFields);
					System.out.println("Queue Json is: " + queueJson);
					result.setResult(true);
				} else { // build error Json
					errorJson = buildDatabaseOrQueueingError("A database insert failed.");
				}
				
				System.out.println("Queueing Processed: " + result.getResult());
				
				if (result.getResult()) {
					String entityToJson = goodResponse(savedEntity, requestStringBuilderContainer, null);
				    return Mono.just(ResponseEntity.status(HttpStatus.CREATED).headers(createResponseHeader(request)).body(entityToJson));
				} else {
					status.setRollbackOnly(); // Mark for rollback
				    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson));
				}
			}); // end the flatMap
		}).last(); // end the execute
	}
	
	public Mono<ResponseEntity<Object>> updateTransactionResult( ServerHttpRequest request, 
			TaskEntity updatedTaskEntity,
			TransactionalOperator transactionalOperator,
			StringBuilderContainer requestStringBuilderContainer)
	{
		
		// Wrap the operations in a transaction	
		// Using the Lambda Implementation of the Callback doInTransaction(ReactiveTransaction status) method 
		return transactionalOperator.execute(status -> {  

			// Perform updates/inserts within this block
				
			// support CORS - createResponseHeader(request);
			// flatMap is designed for asynchronous, one-to-many transformations
			// map is designed for synchronous, one-to-one data transformations
			
			// .map() automatically converts the return Object to a Mono 
			// .flatMap() does not
			
			return taskRepository.save(updatedTaskEntity)
	        .doOnError(ex -> {
	            // This block is executed if an error is thrown within the transaction
	            System.out.println("update Transaction failed: " + ex.getMessage());
	            // The transaction will be marked for rollback automatically
	            if (!status.isRollbackOnly()) {
	            	status.setRollbackOnly();
	            }
	        }) // end doOnError	        
			.<ResponseEntity<Object>>map(savedEntity -> {

	        	// In the future write entityToJson to Kafka or RabbitMQ.
				// Another process(maybe mulesoft or AWS Lambda or Apache Flink) can read the queue
				//  and store the json, in an Iceberg table living in AWS S3.
				// The S3 bucket would store the json, using the OLAP data lake format parquet.
				// Then snowflake can use it.
				QueueResult result = new QueueResult(savedEntity, false);
	            	
				String errorJson = null;
				if (!status.isRollbackOnly()) { // check if the database update worked
					NonModelAdditionalFields additionalFields = new NonModelAdditionalFields(
						"T-Mobile", OperationEnum.UPDATE.getValue());
					additionalFields.addUpdateInfo("String", "Tasks", "task_status", "taskStatus");
					additionalFields.addUpdateInfo("ZonedDateTime", "Tasks", "task_last_update_date", " taskLastUpdateDate");
					String queueJson = goodResponse(savedEntity, requestStringBuilderContainer, additionalFields);
					System.out.println("Queue Json is: " + queueJson);
					result.setResult(true);
				} else { // build error Json
					errorJson = buildDatabaseOrQueueingError("A database update failed for Id: " + savedEntity.getId());
				}
				
				System.out.println("Queueing Processed: " + result.getResult());
				
				if (result.getResult()) {
					String entityToJson = goodResponse(savedEntity, requestStringBuilderContainer, null);
				    return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(entityToJson);
				} else {
					status.setRollbackOnly(); // Mark for rollback
				    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson);
				}
			}); // end the map, automatically converts the return Object to a Mono
		}).last(); // end the execute
	}
	
	public Mono<ResponseEntity<Object>> deleteTransactionResult( ServerHttpRequest request, 
			Mono<TaskEntity> taskEntityToDelete,
			TransactionalOperator transactionalOperator,
			StringBuilderContainer requestStringBuilderContainer)
	{
		
		// Wrap the operations in a transaction	
		// Using the Lambda Implementation of the Callback doInTransaction(ReactiveTransaction status) method 
		return transactionalOperator.execute(status -> {  

			// Perform updates/inserts within this block
				
			// support CORS - createResponseHeader(request);
			// flatMap is designed for asynchronous, one-to-many transformations
			// map is designed for synchronous, one-to-one data transformations
			
			// .map() automatically converts the return Object to a Mono 
			// .flatMap() does not
			
            return taskEntityToDelete.flatMap(fetchedTask -> taskRepository.delete(fetchedTask)
            		.then(Mono.just(fetchedTask)) // 3. Return a Mono of the original object 
            )
 	        .doOnError(ex -> {
	            // This block is executed if an error is thrown within the transaction
	            System.out.println("delete Transaction failed: " + ex.getMessage());
	            // The transaction will be marked for rollback automatically
	            if (!status.isRollbackOnly()) {
	            	status.setRollbackOnly();
	            }
	        }) // end doOnError	   
			.<ResponseEntity<Object>>map(deleteReturn -> {
				
				RowDelete rowDeleteInfo = (RowDelete) getBean(ROW_DELETE_BEAN);
		    	rowDeleteInfo.setTimestamp(ZonedDateTimeEnum.INSTANCE.now());
		    	String message = buildRowDeleteMessage(TaskImpl.NOT_FOUND_TABLE_NAME, deleteReturn.getId());
		    	rowDeleteInfo.setMessage(message);
		    	rowDeleteInfo.setId(deleteReturn.getId());
				

	        	// In the future write entityToJson to Kafka or RabbitMQ.
				// Another process(maybe mulesoft or AWS Lambda or Apache Flink) can read the queue
				//  and store the json, in an Iceberg table living in AWS S3.
				// The S3 bucket would store the json, using the OLAP data lake format parquet.
				// Then snowflake can use it.
				QueueResult result = new QueueResult(deleteReturn, false);
	            	
				String errorJson = null;
				if (!status.isRollbackOnly()) { // check if the database update worked
					NonModelAdditionalFields additionalFields = new NonModelAdditionalFields(
						"T-Mobile", OperationEnum.DELETE.getValue());
					String queueJson = goodResponse(deleteReturn, requestStringBuilderContainer, additionalFields);
					System.out.println("Queue Json is: " + queueJson);
					result.setResult(true);
				} else { // build error Json
					errorJson = buildDatabaseOrQueueingError("A database delete failed for Id: " + deleteReturn.getId());
				}
				
				System.out.println("Queueing Processed: " + result.getResult());
				
				if (result.getResult()) {
					String entityToJson = goodResponse(rowDeleteInfo, requestStringBuilderContainer, null);
				    return ResponseEntity.status(HttpStatus.OK).headers(createResponseHeader(request)).body(entityToJson);
				} else {
					status.setRollbackOnly(); // Mark for rollback
				    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson);
				}
			}); // end the map, automatically converts the return Object to a Mono
		}).last(); // end the execute
	}
	
	// sample code to save
	
	//				tempMono.flatMap(task -> taskRepository.save(task))
	//				.doOnSuccess(savedEntity -> {
	//					asyncPublishToQueue(Mono.just(savedEntity));				
	//				})
	//				.doOnError(e -> status.setRollbackOnly()) // Optional manual rollback
	
}
