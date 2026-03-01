package springboot.enums;

public enum OperationEnum {
    // 1. Define constants with associated string values
    CREATE("Create"), UPDATE("Update"), DELETE("Delete");
	
    private final String value;

    // 2. Private constructor
    OperationEnum(String value) {
        this.value = value;
    }

    // 3. Getter method
    public String getValue() {
        return value;
    }
    
}
