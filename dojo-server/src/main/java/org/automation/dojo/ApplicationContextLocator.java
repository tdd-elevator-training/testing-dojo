package org.automation.dojo;

import org.automation.dojo.web.model.ShopService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author serhiy.zelenin
 */
// TODO удалить при переходе на спринг полностью
public class ApplicationContextLocator implements ApplicationContextAware {

    static {
        ApplicationContextLocator.getInstance().setApplicationContext(new FileSystemXmlApplicationContext("classpath:/org/automation/dojo/applicationContext.xml"));
    }

    private static ApplicationContextLocator instance;
    private ApplicationContext context;
    private Map<String, Object> mocks = new HashMap<String, Object>();

    private static synchronized ApplicationContextLocator getInstance() {
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
    public static <X> X getBean(String beanName) {
        if (instance.mocks.get(beanName) == null) {
            return (X) instance.context.getBean(beanName);
        } else {
            return (X) instance.mocks.get(beanName);
        }
    }

    public static <X> X getBean(Class<X> clazz) {
        return instance.context.getBean(clazz);
    }

    // TODO пока не перейду полностью наспринг, этот метод использую для тестовых целей
    public static void mock(String serviceName, Object mock) {
        instance.mocks.put(serviceName, mock);
    }
}