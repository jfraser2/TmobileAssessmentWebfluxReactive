package springboot.dto.processing;

public class QueueResult {

	private Object dbEntity;
	private Boolean result;
	
	public QueueResult() {
		
	}
	
	public QueueResult(Object dbEntity, Boolean result) {
		super();
		this.dbEntity = dbEntity;
		this.result = result;
	}

	public Object getDbEntity() {
		return dbEntity;
	}

	public void setDbEntity(Object dbEntity) {
		this.dbEntity = dbEntity;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}
	
}
