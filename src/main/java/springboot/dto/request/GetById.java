package springboot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class GetById {
	
	@NotBlank(message = "Id must not be blank")
	@Pattern(regexp = "[0-9]+", message = "Invalid Id. Only Numbers allowed")
	private String id;

	public GetById(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
