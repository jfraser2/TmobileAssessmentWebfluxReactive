package springboot.configurations;

import java.util.List;

//import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig
{
	
	private static final String lineSeparator = System.getProperties().getProperty("line.separator");
	
    @Bean
    public OpenAPI myOpenAPI() {
    	
    		Contact aContact = new Contact();
        aContact.setName("Joe Fraser");
        aContact.setUrl("https://www.linkedin.com/in/joe-fraser-333127b/"); // url: linkedin, facebook
        aContact.setEmail("jfraser2@yahoo.com"); // email
            
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");
/*        
        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment"); 
*/
        
        String descriptionText = "Request and Response format is JSON, Request validation is: jakarta validation enhanced with a Framework." + lineSeparator
    			+ "Since there are so many ways to do Security. I choose a simple one for this project." + lineSeparator
    			+ "The user must put a JWT Token in the request header or in a request cookie (will make the AngularJS folks happy)." + lineSeparator
    			+ "JWT Tokens copied directly from the site http://jwt.io may not work. Why you ask?" + lineSeparator
    			+ "On the Windows Platform the Character set gets changed, it is not longer UTF-8, it becomes Cp1252 (Eclipse Project Default)." + lineSeparator
    			+ "No worries, a Junit Test was made to build Tokens. It is called BuildJwtToken. For Swagger usage, copy the token from the Eclipse project Console instead." + lineSeparator
			+ "The Secret to making the JUnit produce a UTF-8 token is to set the Windows Environment Variable JAVA_TOOL_OPTIONS to value -Dfile.encoding=UTF-8"; 
        
        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");        

//        License apacheLicense = new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0");        
        
        Info info = new Info()
            .title("Available REST Services")
            .version("1.0.0")
            .contact(aContact)
            .description(descriptionText)
//            .termsOfService("https://www.bezkoder.com/terms")
            .license(mitLicense);
        
        return new OpenAPI().info(info).servers(List.of(devServer));    	
    	
    }
 

}
