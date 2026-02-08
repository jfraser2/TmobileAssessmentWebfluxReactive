package springboot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateTaskStatus {

	@NotNull(message = "Id must not be null")
	private Long id;
	
	@NotBlank(message = "New Task status must not be blank")
	@Size(max = 55, message="Max status Length is 55 characters")
	private String newTaskStatus;

	public UpdateTaskStatus() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNewTaskStatus() {
		return newTaskStatus;
	}

	public void setNewTaskStatus(String newTaskStatus) {
		this.newTaskStatus = newTaskStatus;
	}
	
}
