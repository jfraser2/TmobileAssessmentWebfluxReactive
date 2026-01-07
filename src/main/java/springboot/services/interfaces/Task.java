package springboot.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;

import reactor.core.publisher.Mono;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.dto.request.CreateTask;

public interface Task {
	
	public Mono<ResponseEntity<Object>> findByTaskStatus(String taskStatus, ServerHttpRequest request, StringBuilderContainer requestStringBuilderContainer);
	public Mono<ResponseEntity<Object>> findAll(ServerHttpRequest request, StringBuilderContainer requestStringBuilderContainer);
	
	public Mono<ResponseEntity<Object>> buildAndPersistTaskEntity(CreateTask createTaskRequest, ServerHttpRequest request, StringBuilderContainer requestStringBuilderContainer);
	
	public Mono<ResponseEntity<Object>> findByTaskId(Long id, ServerHttpRequest request, StringBuilderContainer requestStringBuilderContainer);

}
