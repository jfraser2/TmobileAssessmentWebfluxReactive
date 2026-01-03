package springboot.dto.validation.exceptions;

public class DatabaseRowNotFoundException
	extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5601843022086989457L;
	private Long rowId;
	
	public DatabaseRowNotFoundException(String exceptionText) {
		super(exceptionText);
		this.rowId = null;
	}

	public DatabaseRowNotFoundException(String exceptionText, Long rowId) {
		super(exceptionText);
		this.rowId = rowId;
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

}
