package org.automation.dojo.web.servlet;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class RequestWorker {

    private List<String> priceOptions = Arrays.asList("", "more than", "less than");
    private HttpServletRequest request;

    public RequestWorker(HttpServletRequest request) {
        this.request = request;
    }

    public void setRecords(List<Record> filtered) {
        request.setAttribute("records", filtered);
    }

    public void noResultsFound() {
        request.setAttribute("no_results", true);
    }

    public void saveFormState() {
        request.setAttribute("price_options", priceOptions);

        request.setAttribute("price_option", getPriceOption());
        request.setAttribute("price", getStringPrice());
        request.setAttribute("search_text", getSearchText());
    }

    public String getSearchText() {
        return request.getParameter("search_text");
    }

    public String getPriceOption() {
        return request.getParameter("price_option");
    }

    public Double getPrice() {
        String priceString = getStringPrice();
        if (isEmpty(priceString)) {
            return 0d;
        }
        return Double.valueOf(priceString);
    }

    public String getStringPrice() {
        return request.getParameter("price");
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public int getPriceOptionIndex() {
        return priceOptions.indexOf(getPriceOption());
    }
}
