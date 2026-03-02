package springboot.dto.processing;

public class UpdateInfo {
	
	// This should be enough info to generate a SQL 
	// statement on a remote Warehouse System(OLAP)
	private String dataType;
	private String tableName;
	private String fieldName;
	private String oldValue;
	private String newValue;
	
	public UpdateInfo() {
		
	}

	public UpdateInfo(String dataType, String tableName, String fieldName, String oldValue, String newValue) {
		super();
		this.dataType = dataType;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

}
