package springboot.autowire.helpers;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PreDestroy;

public class StringContainer {
	
	private List<String> stringList;
	
	public StringContainer()
	{
		this.stringList = new ArrayList<String>();
	}
	
	// Since it is AutoWired clear the array before you use it
	public void clearStringList()
	{
		// clear the List
		this.stringList.clear();
	}
	
	public List<String> getStringList() {
		return this.stringList;
	}

	public int size()
	{
		return this.stringList.size();
	}
	
	public void addToList(String aString)
	{
		if (null != aString && aString.length() > 0)
		{
			this.stringList.add(aString);
		}
		
	}
	
    @PreDestroy
    public void onDestroy() {
    	// give Memory Back to the JVM, when the Request is over
    	clearStringList();    	
    	this.stringList = null;
        System.out.println("String Container is destroyed!!!");
    }    

}
