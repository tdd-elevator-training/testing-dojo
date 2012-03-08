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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddExistingItemWithPriceLessThanEnteredBugTest extends BugTest {

    private AddExistingItemWithPriceLessThanEntered bug;

    @Before
    public void initBug() {
        bug = new AddExistingItemWithPriceLessThanEntered();
    }
}

