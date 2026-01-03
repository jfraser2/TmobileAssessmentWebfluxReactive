package springboot.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;
import springboot.dto.request.CreateTask;
import springboot.entities.TaskEntity;
import springboot.enums.ZonedDateTimeEnum;
import springboot.repositories.TaskRepository;
import springboot.services.interfaces.Task;

@Service
public class TaskImpl
	implements Task
{
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public  List<TaskEntity> findByTaskStatus(String taskStatus) {
		List<TaskEntity> retVar = null;
		
		retVar = taskRepository.findByTaskStatus(taskStatus)
				 .collectList()         // Converts Flux<TaskEntity> to Mono<List<TaskEntity>>
				 .map(task-> { return task; });
		
		
		return retVar;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<TaskEntity> findAll() {
		List<TaskEntity> retVar = null;
		
		List<TaskEntity> usne = taskRepository.findAll();
		if (null != usne)
			retVar = usne;
		
		return retVar;
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TaskEntity persistData(TaskEntity taskEntity) {
		
		TaskEntity retVar = null;
		
		try {
			if (null != taskEntity) {
				retVar = taskRepository.save(taskEntity);
			}	
		} catch (Exception e) {
			retVar = null;
		}
		
		return retVar;
	}

	@Override
	public TaskEntity buildTaskEntity(CreateTask createTaskRequest) {
		
		TaskEntity retVar = null;
		
		try {
			String taskName = createTaskRequest.getTaskName();
			String taskStatus = createTaskRequest.getTaskStatus();
			if (null != taskName && taskName.length() > 0 &&
				null != taskStatus && taskStatus.length() > 0)
			{
				retVar = new TaskEntity();
				retVar.setTaskName(taskName);
				retVar.setTaskDescription(createTaskRequest.getTaskDescription());
				retVar.setTaskStatus(taskStatus);
			    ZonedDateTime zonedDateTime = ZonedDateTimeEnum.INSTANCE.now();
				retVar.setTaskCreateDate(zonedDateTime);
			}
		} catch (Exception e) {
			retVar = null;
		}
		
		return retVar;
	}

}
