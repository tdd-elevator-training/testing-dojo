package org.automation.dojo.web.servlet;

import org.automation.dojo.web.model.Record;
import org.automation.dojo.web.model.ShopService;
import org.automation.dojo.web.model.ShopServiceFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

public class SearchController extends Controller{

    private ShopService service;

    public SearchController() {
        this.service = ShopServiceFactory.gtInstance();
    }

	@Override
	public String doAction() throws ServletException, IOException 
	{
        String foundString = request.getParameter("search_text");

        List<Record> filtered = service.select(foundString);

        if (filtered.isEmpty()) {
            filtered = service.select("");
            request.setAttribute("no_results", true);
        }

        request.setAttribute("records", filtered);

		return "search.jsp";
	}
}