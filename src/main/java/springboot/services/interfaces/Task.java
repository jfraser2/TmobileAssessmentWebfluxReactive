package springboot.services.interfaces;

import java.util.List;

import springboot.dto.request.CreateTask;
import springboot.entities.TaskEntity;

public interface Task {
	
	public List<TaskEntity> findByTaskStatus(String taskStatus);
	public List<TaskEntity> findAll();
	
	public TaskEntity buildTaskEntity(CreateTask createTaskRequest);
	public TaskEntity persistData(TaskEntity taskEntity);

}
