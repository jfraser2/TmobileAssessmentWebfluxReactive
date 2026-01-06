package springboot.dto.response;

public class ResultStatus {
	
	private String requestStatus;
	
	public ResultStatus() {
		
	}

	public ResultStatus(String status) {
		super();
		this.requestStatus = status;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String status) {
		this.requestStatus = status;
	}

	
}
