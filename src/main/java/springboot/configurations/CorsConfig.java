package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * info URL: https://jaxenter.com/spring-boot-tutorial-rest-services-and-microservices-135148.html
 * 
 */

@Configuration
//@EnableWebMvc
public class CorsConfig
{
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
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
        };
    }		
    
}
