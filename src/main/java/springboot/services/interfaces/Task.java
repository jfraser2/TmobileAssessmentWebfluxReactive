package springboot.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;

import reactor.core.publisher.Mono;
import springboot.dto.request.CreateTask;
import springboot.dto.request.UpdateTaskStatus;

public interface Task {
	
	public Mono<ResponseEntity<Object>> findByTaskStatus(String taskStatus, ServerHttpRequest request);
	public Mono<ResponseEntity<Object>> findAll(ServerHttpRequest request);
	
	public Mono<ResponseEntity<Object>> buildAndPersistTaskEntity(CreateTask createTaskRequest, ServerHttpRequest request);
	
	public Mono<ResponseEntity<Object>> findByTaskId(Long id, ServerHttpRequest request);

	public Mono<ResponseEntity<Object>> updateTaskStatus(UpdateTaskStatus updateTaskStatus, ServerHttpRequest request);
	public Mono<ResponseEntity<Object>> deleteTask(Long taskId, ServerHttpRequest request);
	
}
