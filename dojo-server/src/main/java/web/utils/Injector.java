package web.utils;

import java.lang.reflect.Field;

public final class Injector {

	public static <T> T injectAll(T object, Object injectible)
	{
	    final Field[] declaredFields = object.getClass().getDeclaredFields();
        final Class<?> injectibleClass = injectible.getClass();
        
        boolean injected = false;
        
        for (Field field : declaredFields) {
            if (Object.class.equals(field.getType())) {
                continue;
            }
            
            if (!field.getType().isAssignableFrom(injectibleClass)) {
                continue;
            }
            
            field.setAccessible(true);
            
            try {
                field.set(object, injectible);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Bla bla bla: ", e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Bla bla bla: ", e);
            }
            
            field.setAccessible(false);
            
            injected = true;
        }
        
        if (!injected) {
            throw new IllegalArgumentException("Bla bla bla.");
        }
        
        return object;
    }
}


