package org.automation.dojo.web.servlet;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.model.ShopServiceFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SearchController extends Controller {

    private ShopService service;

    public SearchController() {
        this.service = ShopServiceFactory.gtInstance();
    }

    @Override
    public String doAction() throws ServletException, IOException {
        RequestWorker request = new RequestWorker(super.request);

        request.saveFormState();

        String foundString = request.getSearchText();
        if (foundString != null) {
            List<Record> result = service.select(foundString, request.getPriceOptionIndex(), request.getPrice());

            if (result.isEmpty()) {
                result = service.select("", ShopService.IGNORE, 0);
                request.noResultsFound();
            }

            request.setRecords(result);
        }

        return "search.jsp";
    }






}