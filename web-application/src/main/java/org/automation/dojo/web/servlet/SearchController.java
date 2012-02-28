package org.automation.dojo.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

public class SearchController extends Controller{
	
	@Override
	public String doAction() throws ServletException, IOException 
	{		
		return "search.jsp";
	}	
}