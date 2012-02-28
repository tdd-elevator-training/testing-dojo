package org.automation.dojo.web.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

public class SearchController extends Controller{

	@Override
	public String doAction() throws ServletException, IOException 
	{
        List<String> list = new LinkedList<String>();
        list.add("Mouse 1");
        list.add("Mouse 2");
        list.add("Mouse 3");
        list.add("Mouse 4 - the best mouse!");
        list.add("Monitor 1");
        list.add("Monitor 2");
        list.add("Monitor 3 - the best monitor!");

        String foundString = request.getParameter("search_text");

        request.setAttribute("records", filter(list, foundString));

		return "search.jsp";
	}

    private List<String> filter(List<String> list, String foundString) {
        List<String> result = new LinkedList<String>();
        for (String record : list) {
            if (record.toLowerCase().contains(foundString.toLowerCase())) {
                result.add(record);
            }
        }
        return result;
    }
}