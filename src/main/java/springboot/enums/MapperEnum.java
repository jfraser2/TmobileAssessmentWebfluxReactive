package springboot.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum MapperEnum {
	INSTANCE;
	private final ObjectMapper mapper = new ObjectMapper();

	private MapperEnum() {
	    // Perform any configuration on the ObjectMapper here.
	    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // exclude null values
	}

	public ObjectMapper getObjectMapper() {
		return mapper;
	}
	
}
