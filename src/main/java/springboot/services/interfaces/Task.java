package springboot.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;
import springboot.dto.request.CreateTask;
import springboot.entities.TaskEntity;

public interface Task {
	
	public Mono<ResponseEntity<Object>> findByTaskStatus(String taskStatus);
	public Mono<ResponseEntity<Object>> findAll();
	
	public Mono<ResponseEntity<Object>> buildTaskEntity(CreateTask createTaskRequest);

}
