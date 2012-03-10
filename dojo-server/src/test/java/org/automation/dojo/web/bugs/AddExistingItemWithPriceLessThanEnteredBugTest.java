package org.automation.dojo.web.bugs;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddExistingItemWithPriceLessThanEnteredBugTest extends BugTest {

    private AddExistingItemWithPriceLessThanEnteredBug bug;

    @Before
    public void initBug() {
        bug = BugsFactory.getBug(AddExistingItemWithPriceLessThanEnteredBug.class);
    }

    @Test
    public void shouldWorkOnlyWhenSearchByPriceMoreThan(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record3, record4);
        tryToFoundMoreThan(String.valueOf(record3.getPrice()));
        filteredByPrice(record1, record2);

        bug.apply(request);

        assertElementInResultList(record1, record3, record4);
    }

    @Test
    public void shouldNotWorkOnlyWhenSearchByPriceLessThan(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record3, record4);
        tryToFoundLessThan(String.valueOf(record3.getPrice()));
        filteredByPrice(record1, record2);

        bug.apply(request);

        assertElementInResultList(record3, record4);
    }

    @Test
    public void shouldNotWorkWhenEmptyPriceString(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record3, record4);
        tryToFoundMoreThan("");
        filteredByPrice(record1, record2);

        bug.apply(request);

        assertElementInResultList(record3, record4);
    }

    @Test
    public void shouldWorkOnlyWhenFoundSomeRecords(){
        tryToFoundByString();
        nothingToFound();
        weHaveSomeResults(record3, record4);
        tryToFoundMoreThan(String.valueOf(record3.getPrice()));
        filteredByPrice(record1, record2);

        bug.apply(request);

        assertElementInResultList(record3, record4);
    }

    @Test
    public void shouldWorkOnlyWhenListIsNotEmpty(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults();
        tryToFoundMoreThan(String.valueOf(record3.getPrice()));
        filteredByPrice(record1, record2);

        bug.apply(request);

        assertElementInResultList();
    }

    @Test
    public void shouldWorkOnlyWhenListIsNotNull(){
        tryToFoundByString();
        foundSomeRecords();
        when(request.getRecords()).thenReturn(null);
        tryToFoundMoreThan(String.valueOf(record3.getPrice()));
        filteredByPrice(record1, record2);

        bug.apply(request);

        assertElementInResultList();
    }

    @Test
    public void shouldWorkOnlyWhenHaveSomeRecordsLessThan(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record1, record2);
        tryToFoundMoreThan(String.valueOf(record1.getPrice()));
        filteredByPrice();

        bug.apply(request);

        assertElementInResultList(record1, record2);
    }


}

