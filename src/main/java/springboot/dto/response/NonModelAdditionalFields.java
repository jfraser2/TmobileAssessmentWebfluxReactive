package springboot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import springboot.dto.processing.UpdateInfo;
import springboot.enums.ZonedDateTimeEnum;
import springboot.errorHandling.helpers.ListUpdateInfoConverter;

public class NonModelAdditionalFields {
	
	private String source;
	private String operation;
	private String timeZoneUsed;
	private String dateFormatUsed;
	@JsonSerialize(using = ListUpdateInfoConverter.class)
	private List<UpdateInfo> updateData = null; // Lazy loaded

	public NonModelAdditionalFields() {
		super();
		this.timeZoneUsed = ZonedDateTimeEnum.INSTANCE.getZoneIdAsString();
		this.dateFormatUsed = ZonedDateTimeEnum.INSTANCE.DATE_FORMAT3;
	}
	
	public NonModelAdditionalFields(String source, String operation) {
		super();
		this.source = source;
		this.operation = operation;
		this.timeZoneUsed = ZonedDateTimeEnum.INSTANCE.getZoneIdAsString();
		this.dateFormatUsed = ZonedDateTimeEnum.INSTANCE.DATE_FORMAT3;
	}
	
	public NonModelAdditionalFields(String source, String operation, String timeZoneUsed) {
		super();
		this.source = source;
		this.operation = operation;
		this.timeZoneUsed = timeZoneUsed;
		this.dateFormatUsed = ZonedDateTimeEnum.INSTANCE.DATE_FORMAT3;
	}
	
	public NonModelAdditionalFields(String source, String operation, String timeZoneUsed, String dateFormatUsed) {
		super();
		this.source = source;
		this.operation = operation;
		this.timeZoneUsed = timeZoneUsed;
		this.dateFormatUsed = dateFormatUsed;
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
	
	public String getTimeZoneUsed() {
		return timeZoneUsed;
	}

	public void setTimeZoneUsed(String timeZoneUsed) {
		this.timeZoneUsed = timeZoneUsed;
	}
	
	public String getDateFormatUsed() {
		return dateFormatUsed;
	}

	public void setDateFormatUsed(String dateFormatUsed) {
		this.dateFormatUsed = dateFormatUsed;
	}
	
	public void addUpdateInfo(UpdateInfo anUpdateRec) {
		if (null == updateData) {
			this.updateData = new ArrayList<>();
		}
		
		this.updateData.add(anUpdateRec);
	}
	
	public void addUpdateInfo(String dataType, String tableName, String dbFieldName, String jsonFieldName)
	{
		if (null == updateData) {
			this.updateData = new ArrayList<>();
		}
		
		UpdateInfo updateData = new UpdateInfo(
			dataType,
			tableName,
			dbFieldName,
			jsonFieldName
		);
		
		this.updateData.add(updateData);
	}

}
