package org.automation.dojo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author serhiy.zelenin
 */
public class ApplicationContextLocator implements ApplicationContextAware {

    private static ApplicationContextLocator instance;
    private ApplicationContext context;

    public static synchronized ApplicationContextLocator getInstance() {
        if (instance == null) {
            instance = new ApplicationContextLocator();
        }
        return instance;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @SuppressWarnings({"unchecked"})
    public <X> X getBean(String beanName) {
        return (X) context.getBean(beanName);
    }

    public <X> X getBean(Class<X> clazz) {
        return context.getBean(clazz);
    }

    public static void clearInstance() {
        instance = null;
    }
}
