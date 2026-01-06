package springboot.configurations;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableR2dbcRepositories(basePackages = "springboot.repositories")
@EnableR2dbcAuditing // Optional: if you use R2DBC auditing features
public class R2dbcWithPoolConfig {
	
    @Autowired
    Environment env;
    
    @Bean
    public ConnectionFactory connectionFactory( ) {
    	// example: r2dbc:h2:mem:///testdb?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    	
        ConnectionFactory connectionFactory = new H2ConnectionFactory(
                H2ConnectionConfiguration.builder( )
                        .inMemory("testdb")
                        .username( env.getProperty( "spring.r2dbc.username", "SA" ) )
                        .password( env.getProperty( "spring.r2dbc.password", "" ) )
                        //.options( options )
                        .build( )
        );

    	String envValue = env.getProperty("spring.r2dbc.pool.enabled");
    	System.out.println("Before The H2 Connection Pool Test: " + envValue);
    	
    	Boolean testPoolEnabled = true;
    	if (null != envValue) {
    		testPoolEnabled = Boolean.valueOf(envValue);
    	}
    	
        if (!testPoolEnabled) {
        	System.out.println("created The H2 Connection Pool");
            ConnectionPoolConfiguration connectionPoolConfiguration = ConnectionPoolConfiguration.builder( connectionFactory )
                    .maxIdleTime( Duration.ofSeconds( env.getProperty( "spring.r2dbc.pool.max-idle-time", Long.class, 600L ) ) ) // 10 Minutes
                    .maxAcquireTime( Duration.ofSeconds( env.getProperty( "spring.r2dbc.pool.max-acquire-time", Long.class, 5L ) ) ) // 5 seconds
                    .initialSize( env.getProperty( "spring.r2dbc.pool.initial-size", Integer.class, 10 ) )
                    .maxSize( env.getProperty( "spring.r2dbc.pool.max-size", Integer.class, 25 ) )
                    .validationQuery( env.getProperty( "spring.r2dbc.validation-query", String.class, "SELECT 1" ) )
                    .build( );
            return new ConnectionPool( connectionPoolConfiguration );
        } else {
            return connectionFactory;
        }
    }

}
