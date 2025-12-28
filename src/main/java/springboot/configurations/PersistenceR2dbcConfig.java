package springboot.configurations;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.ValidationMode;

import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.provider.ReactivePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/* https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa */

@Configuration
@EnableR2dbcRepositories(basePackages = "springboot.repositories")
@EnableTransactionManagement
@EnableR2dbcAuditing // Optional: if you use R2DBC auditing features
public class PersistenceR2dbcConfig
{
    // Spring Boot will auto-configure the default ConnectionFactory if properties are set.
    // This explicit bean is only necessary for advanced customization.

    /**
     * Initializes the database schema using schema.sql and populates data using data.sql
     * upon application startup.
     */
	
	
	@Bean(name="entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        // Properties can be read from application.properties or application.yml
        Map<String, Object> properties = new HashMap<>();
        
        // **Key reactive property:** Specify the reactive persistence provider
        properties.put("javax.persistence.provider", ReactivePersistenceProvider.class.getName());

        // Standard database connection properties (from application.properties)
        properties.put("jakarta.persistence.jdbc.url", "r2dbc:h2:mem:testdb");
        properties.put("jakarta.persistence.jdbc.user", "SA");
        properties.put("jakarta.persistence.jdbc.password", "");

        // Other standard Hibernate properties
//        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "none"));
//        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql", "true"));
        additionalProperties(properties);
        
        // Specify entity packages to scan
	    String[] scanPackages = {"springboot.dto.response", "springboot.entities"};
        properties.put("jakarta.persistence.aorm.packages", scanPackages);

        // Build the EntityManagerFactory
        // The "default" name refers to the persistence unit name
        return Persistence.createEntityManagerFactory("TmobileAssessmentReactivePU", properties);
    }

	
	@Bean(name="sessionFactory")
    public Mutiny.SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
        // Adapt the standard EMF to the Hibernate Reactive Mutiny SessionFactory
        return entityManagerFactory.unwrap(Mutiny.SessionFactory.class);
    }
	

	@Bean	
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
	{
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	private void additionalProperties(Map<String, Object> properties)
	{
//	    properties.put("hibernate.ddl-auto", "none");
//	    properties.put("hibernate.ddl-auto", "create-drop");
	    properties.put("hibernate.ddl-auto", "update");
	    properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
	    properties.put("hibernate.current_session_context_class", "thread");
	    properties.put("hibernate.format_sql", "true");
	    properties.put("hibernate.show_sql", "true");
	    properties.put("hibernate.jdbc.lob.non_contextual_creation", "true");
	    properties.put("hibernate.connection.release_mode", "after_transaction");
	    
	}
	
	
}
