package org.automation.dojo.web.bugs;

import org.automation.dojo.BugsQueue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.fest.reflect.core.Reflection.constructor;

public class BugsFactory {

    private static Map<Class, Bug<?>> bugs = new HashMap<Class, Bug<?>>();
    private static int id = 0;

    static {
        registerBug(NullBug.class);

        registerBug(NoResultWhenExpectedBug.class);
        registerBug(AddSomeOtherElementIfListNotEmptyBug.class);
        registerBug(FoundNotExistsProductBug.class);

        registerBug(AddExistingItemWithPriceLessThanEntered.class);
        registerBug(AddExistingItemWithPriceMoreThanEntered.class);

        registerBug(BrokenSortingBug.class);
        registerBug(IgnorePriceSortingOrderBug.class);

        registerBug(SomeRecordsWillNotAddToCart.class);
        registerBug(BrokenChartSum.class);
    }

    private static <T extends Bug<?>> void registerBug(Class<T> bugClass) {
        id++;
        bugs.put(bugClass, constructor().withParameterTypes(int.class)
                .in(bugClass)
                .newInstance(id));
    }

    public static <T extends Bug<?>> T getBug(Class<T> bugClass) {
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
