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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BugTest {
    protected ShopService shop;
    protected RequestWorker request;
    protected Record record1;
    protected Record record2;
    protected Record record3;
    protected Record record4;
    private List<Record> list;

    @Before
    public void setup() {
        record1 = new Record("record 1", 10);
        record2 = new Record("record 2", 20);
        record3 = new Record("record 3", 30);
        record4 = new Record("record 4", 40);

        shop = mock(ShopService.class);
        when(shop.selectByText(anyString())).thenReturn(Arrays.asList(record1, record2, record3, record4));

        ApplicationContextLocator.getInstance().mock("shopService", shop);

        request = mock(RequestWorker.class);
    }

    @After
    public void after() {
        ApplicationContextLocator.getInstance().mock("shopService", null);
    }

    protected void assertElementInResultList(Record... records) {
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
    protected void foundSomeRecords() {
        when(request.isNoResultsFound()).thenReturn(false);
    }

    /**
     * Если ничего не найдено сервисом
     */
    protected void nothingToFound() {
        when(request.isNoResultsFound()).thenReturn(true);
    }

    /**
     * Тут мы говорим, что у нас было что-то найдено в магазине
     * и говорим, что именно.
     */
    protected void weHaveSomeResults(Record... records) {
        list = new LinkedList<Record>(Arrays.asList(records));
        when(request.getRecords()).thenReturn(list);
    }

    /**
     * Тут мы говорим, что был произведен поиск по строке
     */
    protected void tryToFoundByString() {
        when(request.getSearchText()).thenReturn("bla");
    }

    /**
     * Тут мы говорим, что был произведен поиск по цене
     */
    protected void tryToFoundMoreThan(String price) {
        when(request.getStringPrice()).thenReturn(price);
        when(request.getPriceOptionIndex()).thenReturn(ShopService.MORE_THAN);
    }

    protected void tryToFoundLessThan(String price) {
        when(request.getStringPrice()).thenReturn(price);
        when(request.getPriceOptionIndex()).thenReturn(ShopService.LESS_THAN);
    }

    /**
     * Если кто-то захочет пофильтровать список через сервис, то мы ему выдадим че надо
     */
    protected void filteredByPrice(Record...records) {
        when(shop.priceFilter(anyList(), anyInt(), anyDouble())).thenReturn(Arrays.asList(records));
    }
}
