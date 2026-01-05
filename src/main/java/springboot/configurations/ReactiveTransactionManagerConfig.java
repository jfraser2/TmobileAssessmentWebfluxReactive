package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableTransactionManagement
public class ReactiveTransactionManagerConfig {

    @Bean
    ReactiveTransactionManager transactionManager( ConnectionFactory connectionFactory ) {
    	System.out.println("Enabled the Reactive Transaction Manager");

        return new R2dbcTransactionManager( connectionFactory );
    }
    
}
