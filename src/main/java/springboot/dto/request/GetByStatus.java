package springboot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GetByStatus {
	@NotBlank(message = "Task status must not be blank")
	@Size(max = 55, message="Max status Length is 55 characters")
	private String taskStatus;

	public GetByStatus(String taskStatus) {
		
		if ("\"\"".equalsIgnoreCase(taskStatus)) {
			this.taskStatus = null;
		} else {
			this.taskStatus = taskStatus;
		}	
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

}
