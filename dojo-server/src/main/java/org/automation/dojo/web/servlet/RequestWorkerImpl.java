package org.automation.dojo.web.servlet;

import org.automation.dojo.web.model.Record;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class RequestWorkerImpl implements RequestWorker {

    public static List<String> priceOptions = Arrays.asList("", "more than", "less than");
    private HttpServletRequest request;

    public RequestWorkerImpl(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public List<Record> getRecords() {
        return (List<Record>) request.getAttribute("records");
    }

    @Override
    public void setRecords(List<Record> filtered) {
        request.setAttribute("records", filtered);
    }

    @Override
    public boolean isNoResultsFound() {
        Object noResults = request.getAttribute("no_results");
        return (noResults != null && (Boolean)noResults);
    }

    @Override
    public void noResultsFound() {
        request.setAttribute("no_results", true);
    }

    @Override
    public void saveFormState() {
        request.setAttribute("price_options", priceOptions);

        request.setAttribute("price_option", getPriceOption());
        request.setAttribute("price", getStringPrice());
        request.setAttribute("search_text", getSearchText());
    }

    @Override
    public String getSearchText() {
        return request.getParameter("search_text");
    }

    @Override
    public String getPriceOption() {
        return request.getParameter("price_option");
    }

    @Override
    public Double getPrice() {
        String priceString = getStringPrice();
        if (isEmpty(priceString)) {
            return 0d;
        }
        return Double.valueOf(priceString);
    }

    @Override
    public String getStringPrice() {
        return request.getParameter("price");
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    @Override
    public int getPriceOptionIndex() {
        return priceOptions.indexOf(getPriceOption());
    }

    @Override
    public void clearNoResultsFound() {
        request.setAttribute("no_results", false);
    }
}
