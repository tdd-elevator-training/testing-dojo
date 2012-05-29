package org.automation.dojo.web.servlet;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.UserCart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RequestWorkerImpl implements RequestWorker {

    public static List<String> priceOptions = Arrays.asList("", "more than", "less than");
    public static List<String> sortingOptions = Arrays.asList("ascending", "descending");

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
    public void saveSearchTextState() {
        request.setAttribute("search_text", getSearchText());
    }

    @Override
    public void saveSearchPriceState() {
        request.setAttribute("price_search_options", priceOptions);

        request.setAttribute("price_search_option", getPriceSearchOption());
        request.setAttribute("price", getStringPrice());
    }

    @Override
    public void saveSearchPriceSortingState() {
        request.setAttribute("asc_desc_options", sortingOptions);

        setPriceSortingOrderOption(isAsc());
    }

    @Override
    public String getSearchText() {
        return request.getParameter("search_text");
    }

    @Override
    public String getPriceSearchOption() {
        return request.getParameter("price_search_option");
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
        return priceOptions.indexOf(getPriceSearchOption());
    }

    @Override
    public void clearNoResultsFound() {
        request.setAttribute("no_results", false);
    }

    @Override
    public boolean isAsc() {
        String string = getPriceSortingOrderOption();
        return isAsc(string);
    }

    private boolean isAsc(String string) {
        if (isEmpty(string)) {
            return true;
        }
        return sortingOptions.indexOf(string) == 0;
    }

    @Override
    public String getPriceSortingOrderOption() {
        return request.getParameter("price_sorting_order_option");
    }

    @Override
    public void setPriceSortingOrderOption(boolean isAsc) {
        String option = sortingOptions.get((isAsc) ? 0 : 1);
        request.setAttribute("price_sorting_order_option", option);
    }

    @Override
    public List<Integer> getRecordIds() {
        List<Integer> result = new LinkedList<Integer>();
        String[] records = request.getParameterValues("record");
        if (records == null) {
            return result;
        }

        for (String record : records) {
            try {
                result.add(Integer.valueOf(record));
            } catch (NumberFormatException exception) {
                // do nothing TODO но вообще-то надо как-то сообщить Сане :)
            }
        }
        return result;
    }

    @Override
    public boolean isSearchAction() {
        String action = getAction();
        return (action == null) || "search".equals(action);
    }

    private String getAction() {
        return request.getParameter("action");
    }

    @Override
    public boolean isCartAction() {
        return "cart".equals(getAction());
    }

    @Override
    public UserCart getUserCart() {
        HttpSession session = request.getSession();
        Object cart = session.getAttribute("cart");
        if (cart == null) {
            cart = new UserCart();
            session.setAttribute("cart", cart);
        }
        return (UserCart) cart;
    }

    @Override
    public void setTotalSum(double price) {
        request.setAttribute("total_price", String.valueOf(price));
    }

    @Override
    public void setValidatePriceNumber(boolean isEnabled) {
        request.setAttribute("validate_price_number", isEnabled);
    }

    @Override
    public void setSearchTextMaxLength(int maxLength) {
        request.setAttribute("search_text_max_length", maxLength);
    }


}
