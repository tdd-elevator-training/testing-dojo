package web.servlet;

import web.model.Record;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

public class SearchController extends Controller{

	@Override
	public String doAction() throws ServletException, IOException 
	{
        List<Record> list = new LinkedList<Record>();
        list.add(new Record("Mouse 1", 30));
        list.add(new Record("Mouse 2", 50));
        list.add(new Record("Mouse 3", 40));
        list.add(new Record("Mouse 4 - the best mouse!", 66));
        list.add(new Record("Monitor 1", 150));
        list.add(new Record("Monitor 2", 120));
        list.add(new Record("Monitor 3 - the best monitor!", 190));

        String foundString = request.getParameter("search_text");
        List<Record> filtered = sortByPrice(foundTextAtDesciption(list, foundString));
        if (filtered.isEmpty()) {
            request.setAttribute("no_results", true);
            filtered = list;
        }
        request.setAttribute("records", filtered);

		return "search.jsp";
	}

    private List<Record> sortByPrice(List<Record> records) {
        List<Record> result = new LinkedList<Record>(records);

        Collections.sort(result, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return new Double(o1.getPrice()).compareTo(new Double(o2.getPrice()));
            }
        });

        return result;
    }

    private List<Record> foundTextAtDesciption(List<Record> list, String foundString) {
        List<Record> result = new LinkedList<Record>();
        for (Record record : list) {
            if (record.getDescription().toLowerCase().contains(foundString.toLowerCase())) {
                result.add(record);
            }
        }
        return result;
    }
}