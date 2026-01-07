package springboot.enums;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// GsonBuilder Excludes Nulls by Default
public enum GsonEnum {
	INSTANCE;
	private final Gson gsonExcludeNullsPrettyPrint = new GsonBuilder().setPrettyPrinting().create();	
	private final Gson gsonPrettyPrint = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	private final Gson gsonExcludeNullsNoPrettyPrint = new GsonBuilder().create();
	
	private GsonEnum() {
	    // Perform any configuration on the Gson Objects here.
	}

	public Gson getGsonExcludeNullsPrettyPrint() {
		return gsonExcludeNullsPrettyPrint;
	}

	public Gson getGsonPrettyPrint() {
		return gsonPrettyPrint;
	}

	public Gson getGsonExcludeNullsNoPrettyPrint() {
		return gsonExcludeNullsNoPrettyPrint;
	}
	
	public GsonBuilder getGsonBuilder() {
		return new GsonBuilder();
	}
	
	// Adapter Type is usually the class of a Java Field in a Java POJO. Example ZonedDateTime.class
	// adapter Instance would be an instance of any class that implements JsonSerializer<AdapterType>, JsonDeserializer<AdapterType>.
	// Example would be implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime>.
	public GsonBuilder registerTypeAdapter(GsonBuilder gsonBuilder, Type adapterType, Object adapterInstance) {
		
		return gsonBuilder.registerTypeAdapter(adapterType, adapterInstance);
	}
	
	public Gson getGsonExcludedNullsNoPrettyPrint(GsonBuilder aBuilder) {
		return aBuilder.create();
	}
	
	public Gson getGsonNoPrettyPrint(GsonBuilder aBuilder) {
		return aBuilder.serializeNulls().create();
	}

}
