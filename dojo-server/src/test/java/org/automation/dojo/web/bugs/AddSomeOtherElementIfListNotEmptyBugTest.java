package org.automation.dojo.web.bugs;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AddSomeOtherElementIfListNotEmptyBugTest extends BugTest {

    private AddSomeOtherElementIfListNotEmptyBug bug;

    @Before
    public void initBug() {
        bug = BugsFactory.getBug(AddSomeOtherElementIfListNotEmptyBug.class);
    }

    @Test
    public void shouldWorkOnlyIfWeHaveSomeSearchStringButNothingFound(){
        tryToFoundByString();
        nothingToFound();
        weHaveSomeResults(record3, record4);

        bug.apply(request);

        assertElementInResultList(record3, record4);
    }

    @Test
    public void shouldAddToCenterOfList(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record1, record2, record4);

        bug.apply(request);

        assertElementInResultList(record1, record2, record3, record4);
    }

    @Test
    public void shouldAddToEndOfList(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record1, record2);

        bug.apply(request);

        assertElementInResultList(record1, record2, record3);
    }

    @Test
    public void shouldAddToStartOfList(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record3, record4);

        bug.apply(request);

        assertElementInResultList(record1, record3, record4);
    }

    @Test
    public void shouldWorkOnlyIfListNotNull(){
        tryToFoundByString();
        foundSomeRecords();
        when(request.getRecords()).thenReturn(null);

        bug.apply(request);

        assertElementInResultList();
    }

    @Test
    public void shouldWorkOnlyIfListNotEmpty(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults();

        bug.apply(request);

        assertElementInResultList();
    }

    @Test
    public void shouldWorkOnlyIfWeHaveNullSearchString(){
        when(request.getSearchText()).thenReturn(null);
        foundSomeRecords();
        weHaveSomeResults(record3, record4);

        bug.apply(request);

        assertElementInResultList(record3, record4);
    }


}
