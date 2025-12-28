package springboot.repositories;

import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import springboot.entities.TaskEntity;

@Repository
public interface TaskRepository extends ReactiveCrudRepository<TaskEntity, Long>{
    @Query(value = "SELECT entity FROM TaskEntity entity WHERE entity.taskStatus = :taskStatus")
    Flux<TaskEntity> findByTaskStatus(@Param("taskStatus") String taskStatus);
	
}
