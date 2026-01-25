package springboot.controllers.rest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class ControllerBase
	implements ApplicationContextAware
{
	
	private ApplicationContext applicationContext;

	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }	

	protected Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

}
