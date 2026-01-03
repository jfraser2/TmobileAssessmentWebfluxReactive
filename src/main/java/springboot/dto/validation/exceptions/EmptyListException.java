package springboot.dto.validation.exceptions;

public class EmptyListException
	extends Exception
{
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1896610476358234443L;
	protected String className;

	public EmptyListException(String exceptionText, String className) {
		super(exceptionText);
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
