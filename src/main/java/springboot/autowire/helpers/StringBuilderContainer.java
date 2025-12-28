package springboot.autowire.helpers;

import jakarta.annotation.PreDestroy;

public class StringBuilderContainer {
	
	private StringBuilder stringBuilder;
	
	public StringBuilderContainer()
	{
		this.stringBuilder = new StringBuilder("");
	}

	// Since it is AutoWired clear the buffer before you use it
	public void clearStringBuffer()
	{
		// clear the builder
		this.stringBuilder.delete(0, this.stringBuilder.length());
	}
	
	public StringBuilder getStringBuilder() {
		return this.stringBuilder;
	}

    @PreDestroy
    public void onDestroy() {
    	// give Memory Back to the JVM, when the Request is over
    	clearStringBuffer();
    	this.stringBuilder = null;
        System.out.println("StringBuilder Container is destroyed!!!");
    }    
	
}
