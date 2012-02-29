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
    private List<String> priceOptions;

    public SearchController() {
        this.service = ShopServiceFactory.gtInstance();
        priceOptions = Arrays.asList("", "more than", "less than");
    }

    @Override
    public String doAction() throws ServletException, IOException {
        saveFormState();

        String foundString = getSearchText();
        if (foundString != null) {
            List<Record> result = service.select(foundString, getPriceOptionIndex(), getPrice());

            if (result.isEmpty()) {
                result = service.select("", ShopService.IGNORE, 0);
                noResultsFound();
            }

            setRecords(result);
        }

        return "search.jsp";
    }

    private void setRecords(List<Record> filtered) {
        request.setAttribute("records", filtered);
    }

    private void noResultsFound() {
        request.setAttribute("no_results", true);
    }

    private int getPriceOptionIndex() {
        return priceOptions.indexOf(getPriceOption());
    }

    private void saveFormState() {
        request.setAttribute("price_options", priceOptions);

        request.setAttribute("price_option", getPriceOption());
        request.setAttribute("price", getStringPrice());
        request.setAttribute("search_text", getSearchText());
    }

    private String getSearchText() {
        return request.getParameter("search_text");
    }

    private String getPriceOption() {
        return request.getParameter("price_option");
    }

    private Double getPrice() {
        String priceString = getStringPrice();
        if (isEmpty(priceString)) {
            return 0d;
        }
        return Double.valueOf(priceString);
    }

    private String getStringPrice() {
        return request.getParameter("price");
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }
}