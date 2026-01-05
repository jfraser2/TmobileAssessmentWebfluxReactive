 package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/* @SpringBootApplicaton is the combination of
 *  @EnableAutoConfiguration, @ComponentScan and @Configuration annotations.
 */

@SpringBootApplication
@EntityScan(basePackages = {"springboot.entities"})
@ComponentScan(basePackages = {"springboot", "springboot.configurations"})
public class PocAppMain    // Proof of Concept App Main
{
	public static void main(String[] args)
	{
		// default Application context-path is: "/"
		// should be in file application.properties in src/main/resources
//		System.setProperty(ConfigServerReader.APP_NAME_PROPERTY, "RegistrationMicroService");
		String bootEnv = System.getProperty("spring.profiles.active");
		if (null == bootEnv)
		{
			bootEnv = "dev";
		}
		
		SpringApplication theApp = new SpringApplication(PocAppMain.class);
		theApp.setAdditionalProfiles(bootEnv);
	    theApp.run(args);
	    //SpringApplication.run(PocAppMain.class, args);
	}
	
}
