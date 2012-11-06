package org.automation.dojo.web.bugs;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BugFactoryTest {
    
    @Test
    public void shouldUniqueIds(){
        assertNotNull(BugsFactory.getBug(NullBug.class));

        assertNotNull(BugsFactory.getBug(NoResultWhenExpectedBug.class));
        assertNotNull(BugsFactory.getBug(AddSomeOtherElementIfListNotEmptyBug.class));
        assertNotNull(BugsFactory.getBug(FoundNotExistsProductBug.class));
        assertNotNull(BugsFactory.getBug(NoSearchTextMaxLengthBug.class));

        assertNotNull(BugsFactory.getBug(AddExistingItemWithPriceLessThanEnteredBug.class));
        assertNotNull(BugsFactory.getBug(AddExistingItemWithPriceMoreThanEnteredBug.class));
        assertNotNull(BugsFactory.getBug(DisabledPriceValidationBug.class));

        assertNotNull(BugsFactory.getBug(BrokenSortingBug.class));
        assertNotNull(BugsFactory.getBug(IgnorePriceSortingOrderBug.class));

    }

    
}
