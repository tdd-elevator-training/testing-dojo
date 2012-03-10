package org.automation.dojo.web.bugs;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BugFactoryTest {
    
    @Test
    public void shouldUniqueIds(){
        assertEquals(1, BugsFactory.getBug(NullBug.class).getId());

        assertEquals(2, BugsFactory.getBug(NoResultWhenExpectedBug.class).getId());
        assertEquals(3, BugsFactory.getBug(AddSomeOtherElementIfListNotEmptyBug.class).getId());
        assertEquals(4, BugsFactory.getBug(FoundNotExistsProductBug.class).getId());

        assertEquals(5, BugsFactory.getBug(AddExistingItemWithPriceLessThanEnteredBug.class).getId());
        assertEquals(6, BugsFactory.getBug(AddExistingItemWithPriceMoreThanEnteredBug.class).getId());
        assertEquals(7, BugsFactory.getBug(DisabledPriceValidationBug.class).getId());

        assertEquals(8, BugsFactory.getBug(BrokenSortingBug.class).getId());
        assertEquals(9, BugsFactory.getBug(IgnorePriceSortingOrderBug.class).getId());
    }
}
