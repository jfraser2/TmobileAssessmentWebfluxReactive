package springboot.dto.processing;

public class UpdateInfo {
	
	// This should be enough info to generate a SQL 
	// statement on a remote Warehouse System(OLAP)
	private String dataType;
	private String tableName;
	private String dbFieldName;
	private String jsonFieldName; // pointer to an updated field in the json data
	
	public UpdateInfo() {
		
	}

	public UpdateInfo(String dataType, String tableName, String dbFieldName, String jsonFieldName) {
		super();
		this.dataType = dataType;
		this.tableName = tableName;
		this.dbFieldName = dbFieldName;
		this.jsonFieldName = jsonFieldName;
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

	public String getDbFieldName() {
		return dbFieldName;
	}

	public void setDbFieldName(String dbFieldName) {
		this.dbFieldName = dbFieldName;
	}

	public String getJsonFieldName() {
		return jsonFieldName;
	}

	public void setJsonFieldName(String jsonFieldName) {
		this.jsonFieldName = jsonFieldName;
	}
	
}
