package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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

	/* By default the bean name matches the method Name */
    @Bean
    ReactiveTransactionManager transactionManager( ConnectionFactory connectionFactory ) {
    	System.out.println("Enabled the Reactive Transaction Manager");
    	R2dbcTransactionManager aManager =  new R2dbcTransactionManager( connectionFactory );
    	aManager.setEnforceReadOnly(false);
        return aManager;
    }
    
	/* By default the bean name matches the method Name */
    @Bean
	@Scope("prototype") // new bean on every getBean call or Autowired
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager reactiveTransactionManager) {
    	System.out.println("Created Transaction Operator Bean");
    	
    	DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    	definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    	definition.setReadOnly(false); // H2 does not support true or false
        definition.setTimeout(30);  // 30 seconds
        // will join the current Transaction if one exists or start a new one
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        
        return TransactionalOperator.create(reactiveTransactionManager, definition);
//        return TransactionalOperator.create(reactiveTransactionManager);
        
    }  
    
	/* By default the bean name matches the method Name */
    @Bean
	@Scope("prototype") // new bean on every getBean call or Autowired
    public TransactionalOperator readOnlyTransactionalOperator(ReactiveTransactionManager reactiveTransactionManager) {
    	System.out.println("Created ReadOnly Transaction Operator Bean");
    	
    	DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
    	definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    	definition.setReadOnly(false); // H2 does not support true or false
        definition.setTimeout(30);  // 30 seconds
        // will join the current Transaction if one exists or start a new one
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        
        return TransactionalOperator.create(reactiveTransactionManager, definition);
//        return TransactionalOperator.create(reactiveTransactionManager);
    }    
    
    
}
