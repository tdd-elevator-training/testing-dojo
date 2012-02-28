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

        request.setAttribute("records", list);

		return "search.jsp";
	}	
}