package org.automation.dojo;

import org.automation.dojo.web.model.ShopService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @author serhiy.zelenin
 */
// TODO удалить при переходе на спринг полностью
public class ApplicationContextLocator implements ApplicationContextAware {

    private static ApplicationContextLocator instance;
    private ApplicationContext context;
    private Map<String, Object> mocks = new HashMap<String, Object>();

    public static synchronized ApplicationContextLocator getInstance() {
        if (instance == null) {
            instance = new ApplicationContextLocator();
        }
        return instance;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance = this;
        this.context = applicationContext;
    }

    @SuppressWarnings({"unchecked"})
    public <X> X getBean(String beanName) {
        if (mocks.get(beanName) == null) {
            return (X) context.getBean(beanName);
        } else {
            return (X) mocks.get(beanName);
        }
    }

    public <X> X getBean(Class<X> clazz) {
        return context.getBean(clazz);
    }

    public static void clearInstance() {
        instance = null;
    }

    // TODO пока не перейду полностью наспринг, этот метод использую для тестовых целей
    public void mock(String serviceName, Object mock) {
        mocks.put(serviceName, mock);
    }
}
