package springboot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import springboot.dto.processing.UpdateInfo;
import springboot.errorHandling.helpers.ListUpdateInfoConverter;

public class NonModelAdditionalFields {
	
	private String source;
	private String operation;
	@JsonSerialize(using = ListUpdateInfoConverter.class)
	private List<UpdateInfo> updateData = null; // Lazy loaded

	public NonModelAdditionalFields() {
		super();
	}
	
	public NonModelAdditionalFields(String source, String operation) {
		super();
		this.source = source;
		this.operation = operation;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public void addUpdateInfo(UpdateInfo anUpdateRec) {
		if (null == updateData) {
			this.updateData = new ArrayList<>();
		}
		
		this.updateData.add(anUpdateRec);
	}
	
	public void addUpdateInfo(String dataType, String tableName, String fieldName, String newValue)
	{
		if (null == updateData) {
			this.updateData = new ArrayList<>();
		}
		
		UpdateInfo updateData = new UpdateInfo(
			dataType,
			tableName,
			fieldName,
			newValue
		);
		
		this.updateData.add(updateData);
	}

}
