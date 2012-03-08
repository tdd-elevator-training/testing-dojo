package org.automation.dojo.web.bugs;

import org.automation.dojo.ApplicationContextLocator;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.servlet.RequestWorker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AddSomeOtherElementIfListNotEmptyBugTest {

    private ShopService shop;
    private AddSomeOtherElementIfListNotEmptyBug bug;
    private RequestWorker request;
    private Record record1;
    private Record record2;
    private Record record3;
    private Record record4;
    private List<Record> list;


    @Before
    public void setup() {
        record1 = new Record("record 1", 10);
        record2 = new Record("record 2", 20);
        record3 = new Record("record 3", 30);
        record4 = new Record("record 4", 40);

        shop = mock(ShopService.class);
        when(shop.selectByText(anyString())).thenReturn(Arrays.asList(record1, record2, record3, record4));

        bug = new AddSomeOtherElementIfListNotEmptyBug();
        ApplicationContextLocator.getInstance().mock("shopService", shop);

        request = mock(RequestWorker.class);
    }

    @After
    public void after() {
        ApplicationContextLocator.getInstance().mock("shopService", null);
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

    private void assertElementInResultList(Record...records) {
        if (records.length == 0) {
            if (list != null) {
                assertEquals("Список должен быть пуст", "[]", list.toString());
            }
        } else {
            assertEquals("Сортировка нарушена", Arrays.asList(records).toString(), list.toString());
        }
    }

    /**
     * Если что-то найдено сервисом
     */
    private void foundSomeRecords() {
        when(request.isNoResultsFound()).thenReturn(false);
    }

    @Test
    public void shouldWorkOnlyIfWeHaveSomeSearchStringButNothingFound(){
        tryToFoundByString();
        nothingToFound();
        weHaveSomeResults(record3, record4);

        bug.apply(request);

        assertElementInResultList(record3, record4);
    }

    /**
     * Если ничего не найдено сервисом
     */
    private void nothingToFound() {
        when(request.isNoResultsFound()).thenReturn(true);
    }

    @Test
    public void shouldAddToCenterOfList(){
        tryToFoundByString();
        foundSomeRecords();
        weHaveSomeResults(record1, record2, record4);

        bug.apply(request);

        assertElementInResultList(record1, record2, record3, record4);
    }

    /**
     * Тут мы говорим, что у нас было что-то найдено в магазине
     * и говорим, что именно.
     */
    private void weHaveSomeResults(Record ... records) {
        list = new LinkedList<Record>(Arrays.asList(records));
        when(request.getRecords()).thenReturn(list);
    }

    /**
     * Тут мы говорим, что был произведен поиск по строке
     */
    private void tryToFoundByString() {
        when(request.getSearchText()).thenReturn("bla");
    }


}
