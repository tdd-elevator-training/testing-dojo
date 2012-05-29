package org.automation.dojo.web.bugs;

import org.reflections.Reflections;

import java.util.*;

import static org.fest.reflect.core.Reflection.constructor;

public class BugsFactory {

    private static Map<Class, Bug<?>> bugs = new HashMap<Class, Bug<?>>();
    private static int id = 0;

    private static Set<Class<? extends Bug>> getAllBugsInPackage() {
        Reflections reflections = new Reflections(BugsFactory.class.getPackage().getName());

        return reflections.getSubTypesOf(Bug.class);
    }

    private static <T extends Bug<?>> void registerBug(Class<T> bugClass) {
        id++;
        bugs.put(bugClass, constructor().withParameterTypes(int.class)
                .in(bugClass)
                .newInstance(id));
    }

    public static <T extends Bug<?>> T getBug(Class<T> bugClass) {
        if (bugs.size() == 0) {
            Set<Class<? extends Bug>> bugs = getAllBugsInPackage();

            for (Class<? extends Bug> bug : bugs) {
                registerBug(bug);
            }
        }
        if (!bugs.containsKey(bugClass)) {
            throw new IllegalStateException(String.format("Bug %s not found. Please register bug at BugsFactory", bugClass));
        }
        return (T) bugs.get(bugClass);
    }

    public static List<Bug<?>> getBugs(Class<? extends Bug<?>>...classes) {
        List<Bug<?>> result = new LinkedList<Bug<?>>();
        for (Class<? extends Bug<?>> bug : classes) {
            result.add(getBug(bug));
        }
        return result;
    }
}
