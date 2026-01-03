package springboot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTask {
	@NotBlank(message = "Task name must not be blank")
	@Size(max = 55, message="Max name Length is 55 characters")
	private String taskName;

	@Size(max = 55, message="Max description Length is 55 characters")
	private String taskDescription;
	
	@NotBlank(message = "Task status must not be blank")
	@Size(max = 55, message="Max status Length is 55 characters")
	private String taskStatus;

    public CreateTask()
    {
    }
    
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
}
