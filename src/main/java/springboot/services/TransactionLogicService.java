package springboot.services;

//import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.dto.processing.QueueResult;
import springboot.dto.response.NonModelAdditionalFields;
import springboot.entities.TaskEntity;
import springboot.enums.OperationEnum;
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

	public Flux<ResponseEntity<Object>> createTransactionResult( ServerHttpRequest request, 
			TaskEntity task,
			TransactionalOperator transactionalOperator,
			StringBuilderContainer requestStringBuilderContainer)
	{
		
		Flux<ResponseEntity<Object>> tempFlux = null; 
		try {
			// Wrap the operations in a transaction	
			// Using the Lambda Implementation of the Callback doInTransaction(ReactiveTransaction status) method 
			tempFlux = transactionalOperator.execute(status -> {  
	
				// Perform updates/inserts within this block
					
				// support CORS - createResponseHeader(request);
				// flatMap is designed for asynchronous, one-to-many transformations
				// map is designed for synchronous, one-to-one data transformations
				
				// .map() automatically converts the return Object to a Mono 
				// .flatMap() does not
				
				return taskRepository.save(task)
					.<ResponseEntity<Object>>flatMap(savedEntity -> {  
			        	// In the future write entityToJson to Kafka or RabbitMQ.
						// Another process(maybe mulesoft or AWS Lambda) can read the queue and store the json,
						// in an Iceberg table living in AWS S3.
						// The S3 bucket will store the json, using the OLAP data lake format parquet.
						// Then snowflake can use it.
						QueueResult result = new QueueResult(savedEntity, false);
			            	
						String errorJson = null;
						if (!status.isRollbackOnly()) { // check if the database insert worked
							NonModelAdditionalFields additionalFields = new NonModelAdditionalFields();
							additionalFields.setSource("T-Mobile");
							additionalFields.setOperation(OperationEnum.CREATE.getValue());
							String queueJson = goodResponse(savedEntity, requestStringBuilderContainer, additionalFields);
							System.out.println("Queue Json is" + queueJson);
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
					}); // end the map, automatically converts the response to a Mono
			}); // end the execute
		} catch (TransactionException te) {
			String errorJson = buildDatabaseOrQueueingError("A database insert Transacton failed.");
			tempFlux = Flux.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson));
		} catch (RuntimeException re) {
			String errorJson = buildDatabaseOrQueueingError("A database insert Transacton failed.");
			tempFlux = Flux.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(createResponseHeader(request)).body(errorJson));
		}
		
		return tempFlux;
	}
	
	// sample code to save
	
	//				tempMono.flatMap(task -> taskRepository.save(task))
	//				.doOnSuccess(savedEntity -> {
	//					asyncPublishToQueue(Mono.just(savedEntity));				
	//				})
	//				.doOnError(e -> status.setRollbackOnly()) // Optional manual rollback
				
// This code messed up the persistance of the .save()				
				
/*						
			    .onErrorResume(e -> {
			        System.out.println("Save failed: " +  e.getMessage());
			        if (!status.isRollbackOnly()) {
			        	status.setRollbackOnly(); // Mark for rollback
			        }	
			        // Fallback option 1: Return a default value
			        // return Mono.just(defaultValue);
			        // Fallback option 2: Return a custom exception
			        return Mono.just(buildFake(false));
			    })	// end onErrorResume	
*/				    				
	
}
