package springboot.autowire.helpers;

import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentRequestLimit {
    private static final Integer MAX_REQUESTS = 10;
    
    private AtomicInteger requestCounter;
    
    public ConcurrentRequestLimit() {
    	this.requestCounter = new AtomicInteger(0);
    }
    
    public boolean atLimit() {
    	boolean retVar = false;
    	
    	if (requestCounter.get() >= MAX_REQUESTS) {
    		retVar = true;
    	}
    	return retVar;
    }
    
    public int increment() {
    	return this.requestCounter.incrementAndGet();
    }
    
    public int decrement() {
    	return this.requestCounter.decrementAndGet();
    	
    }

}
