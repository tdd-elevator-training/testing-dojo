package org.automation.dojo.web.bugs;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;

public class AddExistingItemWithPriceMoreThanEnteredBugTest extends BugTest {

    private AddExistingItemWithPriceMoreThanEntered bug;

    @Before
    public void initBug() {
        bug = new AddExistingItemWithPriceMoreThanEntered();
    }

    @Test
    public void shouldWorkOnlyWhenSearchByPriceLessThan(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record1, record2);
        tryToFoundLessThan(String.valueOf(record2.getPrice()));
        filteredByPrice(record3, record4);

        bug.apply(request);

        assertElementInResultList(record1, record2, record3);
    }

    @Test
    public void shouldNotWorkOnlyWhenSearchByPriceMoreThan(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record1, record2);
        tryToFoundMoreThan(String.valueOf(record2.getPrice()));
        filteredByPrice(record3, record4);

        bug.apply(request);

        assertElementInResultList(record1, record2);
    }

    @Test
    public void shouldNotWorkWhenEmptyPriceString(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record1, record2);
        tryToFoundLessThan("");
        filteredByPrice(record3, record4);

        bug.apply(request);

        assertElementInResultList(record1, record2);
    }

    @Test
    public void shouldWorkOnlyWhenFoundSomeRecords(){
        tryToFoundByString();
        nothingToFound();
        weHaveSomeResults(record1, record2);
        tryToFoundLessThan(String.valueOf(record2.getPrice()));
        filteredByPrice(record3, record4);

        bug.apply(request);

        assertElementInResultList(record1, record2);
    }

    @Test
    public void shouldWorkOnlyWhenListIsNotEmpty(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults();
        tryToFoundLessThan(String.valueOf(record2.getPrice()));
        filteredByPrice(record3, record4);

        bug.apply(request);

        assertElementInResultList();
    }

    @Test
    public void shouldWorkOnlyWhenListIsNotNull(){
        tryToFoundByString();
        foundSomeRecords();
        when(request.getRecords()).thenReturn(null);
        tryToFoundLessThan(String.valueOf(record2.getPrice()));
        filteredByPrice(record3, record4);

        bug.apply(request);

        assertElementInResultList();
    }

    @Test
    public void shouldWorkOnlyWhenHaveSomeRecordsMoreThan(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record3, record4);
        tryToFoundLessThan(String.valueOf(record4.getPrice()));
        filteredByPrice();

        bug.apply(request);

        assertElementInResultList(record3, record4);
    }


}

