package org.automation.dojo.web.servlet;

import org.apache.commons.codec.binary.StringUtils;
import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.model.ShopServiceFactory;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;

import static org.apache.commons.codec.binary.StringUtils.*;

public class SearchController extends Controller{

    private ShopService service;

    public SearchController() {
        this.service = ShopServiceFactory.gtInstance();
    }

	@Override
	public String doAction() throws ServletException, IOException 
	{
        List<String> priceOptions = Arrays.asList("", "more than", "less than");
        request.setAttribute("price_options", priceOptions);
        String priceOptionString = request.getParameter("price_option");
        request.setAttribute("price_option", priceOptionString);
        request.setAttribute("price", request.getParameter("price"));
        request.setAttribute("search_text", request.getParameter("search_text"));

        String foundString = request.getParameter("search_text");
        if (foundString != null) {
            int priceOption = priceOptions.indexOf(priceOptionString);
            double price = getPrice();
            List<Record> filtered = service.select(foundString, priceOption, price);

            if (filtered.isEmpty()) {
                filtered = service.select("", ShopService.IGNORE, 0);
                request.setAttribute("no_results", true);
            }

            request.setAttribute("records", filtered);
        }

		return "search.jsp";
	}

    private Double getPrice() {
        String priceString = request.getParameter("price");
        if (isEmpty(priceString)) {
            return 0d;
        }
        return Double.valueOf(priceString);
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }
}