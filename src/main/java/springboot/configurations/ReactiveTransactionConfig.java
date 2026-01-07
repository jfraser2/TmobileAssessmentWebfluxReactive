package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableTransactionManagement
public class ReactiveTransactionConfig {

    @Bean
    ReactiveTransactionManager transactionManager( ConnectionFactory connectionFactory ) {
    	System.out.println("Enabled the Reactive Transaction Manager");
        return new R2dbcTransactionManager( connectionFactory );
    }
    
    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager reactiveTransactionManager) {
    	System.out.println("Created Transaction Operator Bean");
    	
    	DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    	definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    	definition.setReadOnly(false);
        definition.setTimeout(30);  // 30 seconds
        
        return TransactionalOperator.create(reactiveTransactionManager, definition);
    }  
    
    @Bean
    public TransactionalOperator readOnlyTransactionalOperator(ReactiveTransactionManager reactiveTransactionManager) {
    	System.out.println("Created ReadOnly Transaction Operator Bean");
    	
    	DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    	definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    	definition.setReadOnly(true);
        definition.setTimeout(30);  // 30 seconds
        
        return TransactionalOperator.create(reactiveTransactionManager, definition);
    }    
    
    
}
