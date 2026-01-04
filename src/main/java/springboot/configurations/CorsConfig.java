package springboot.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * 
 * info URL: https://jaxenter.com/spring-boot-tutorial-rest-services-and-microservices-135148.html
 * 
 */

@Configuration
@EnableWebFlux
//@EnableWebMvc
public class CorsConfig implements WebFluxConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry) {    
//            	System.out.println("Set Up Cors for Angular");
		// Angular Testing from AngularIDE or localhost(manual from a Browser)
		// Support for CRUD
		
	    registry.addMapping("/rest/api/**")
	        .allowedOrigins("http://localhost:4200", "http://localhost:8080")
	        .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Accept", "Content-Type", "Authorization", "api-key", "Access-Control-Allow-Headers")
	        .allowedMethods("POST", "OPTIONS", "GET", "DELETE", "PUT", "PATCH")
	     	.exposedHeaders("Content-Type", "Content-Range", "Access-Control-Allow-Origin")   // headers for the response does not support wildcards
	     	.allowCredentials(false);  
	    
	//            	System.out.println("Set Up Cors for Swagger");
		// Mapping for Swagger Testing
		// Support for CRUD
		
	    registry.addMapping("/swagger-ui/index.html")
	        .allowedOrigins("http://localhost:8080")
	//                    .allowedOrigins("*")
	        .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Access-Control-Allow-Headers", "Accept", "Content-Type", "Vary", "Authorization", "api-key")
	//                    .allowedHeaders("*")
	        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
	//                	.allowedMethods("*")
	     	.exposedHeaders("Content-Type", "Content-Range", "Access-Control-Allow-Origin")   // headers for the response does not support wildcards
	     	.allowCredentials(false);
	    
		// Mapping for H2 Console
	    registry.addMapping("/h2-console/**")
	    	.allowedOrigins("http://localhost:8080")
	//                    .allowedOrigins("*")
	        .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Access-Control-Allow-Headers", "Accept", "Content-Type", "Vary")
	    	.allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
	     	.exposedHeaders("Content-Type", "Content-Range", "Access-Control-Allow-Origin")   // headers for the response does not support wildcards
	    	.allowCredentials(false);  
	    
	    // Add more mappings...
                
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Customize the resource handler for Swagger UI assets
        registry.addResourceHandler("/documentation/webjars/**") // Custom URL path pattern
                .addResourceLocations("classpath:/META-INF/resources/webjars/") // Default location inside the JAR
                .resourceChain(false); // Disable resource chaining (optional, often recommended for WebJars)

        // You might also need to map the main swagger-ui.html or index.html if using older/specific setups
        registry.addResourceHandler("/documentation/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
                
        // For general static resources, you can add more handlers:
        // registry.addResourceHandler("/static/**")
        //         .addResourceLocations("classpath:/static/");
    }    
    
}
