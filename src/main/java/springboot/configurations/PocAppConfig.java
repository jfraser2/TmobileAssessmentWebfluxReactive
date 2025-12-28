package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import springboot.autowire.helpers.ConcurrentRequestLimit;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.StringContainer;
import springboot.autowire.helpers.ValidationErrorContainer;

@Configuration
public class PocAppConfig {
	
	/* By default the bean name matches the method Name */
	@Bean(name="requestValidationErrorsContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ValidationErrorContainer requestValidationErrorsContainer() {
		// each request has a ValidationErrorList
		return new ValidationErrorContainer();
	}
	
	/* Every request has its very own StringBuilder. Within the request,
	 * the same StringBuilder is used over and over. */
	/* Good use of Memory */
	@Bean(name="requestStringBuilderContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public StringBuilderContainer requestStringBuilderContainer() {
		// each request has a StringBuilder
		return new StringBuilderContainer();
	}
	
	/* By default the bean name matches the method Name */
	@Bean(name="requestStringContainer")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public StringContainer requestStringContainer() {
		// each request has a StringList
		return new StringContainer();
	}

	/* By default the bean name matches the method Name */
	@Bean(name="concurrentRequestLimit")
	@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ConcurrentRequestLimit concurrentRequestLimit() {
		// each application has a ConcurrentRequestLimit
		return new ConcurrentRequestLimit();
	}
	
}
