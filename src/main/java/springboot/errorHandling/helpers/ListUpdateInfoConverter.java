package springboot.errorHandling.helpers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import springboot.dto.processing.UpdateInfo;

public class ListUpdateInfoConverter
	extends JsonSerializer<Object>
{

	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
			throws IOException
	{
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<UpdateInfo> fooList = (List<UpdateInfo>) value;
		
		try {
			if (null != fooList && fooList.size() > 0)
			{
		        jgen.writeStartArray(); // [
	        
		        UpdateInfo fooRec = fooList.get(0);
//		        System.out.println("fooRec is: " + fooRec.toString());
	            jgen.writeObject(fooRec);
	       
		        for (int i = 1; i < fooList.size(); i++)
		        {
		            fooRec = fooList.get(i);
		            jgen.writeObject(fooRec);
//			        System.out.println("fooRec is: " + fooRec.toString());
		        }
		        jgen.writeEndArray(); //]
			}
		}
		catch (IOException ioe)
		{
//			System.out.println("Couldn't Serialize Array");
		}
		return;
	}

}
