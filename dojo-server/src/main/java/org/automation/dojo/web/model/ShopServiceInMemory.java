package org.automation.dojo.web.model;

import java.io.Serializable;
import java.util.*;

public class ShopServiceInMemory implements ShopService, Serializable {

    private List<Record> data;
    private Map<String, List<Record>> userCard;

    public ShopServiceInMemory() {
        data = new LinkedList<Record>();
        data.add(new Record(1, "Mouse 1", 30));
        data.add(new Record(2, "Mouse 2", 50));
        data.add(new Record(3, "Mouse 3", 40));
        data.add(new Record(4, "Mouse 4 - the best mouse!", 66));
        data.add(new Record(5, "Monitor 1", 150));
        data.add(new Record(6, "Monitor 2", 120));
        data.add(new Record(7, "Monitor 3 - the best monitor!", 190));

        userCard = new HashMap<String, List<Record>>();
    }

    @Override
    public List<Record> selectByText(String foundString) {
        return foundTextAtDescription(copy(data), foundString);     // TODO testing copying
    }

    private List<Record> copy(List<Record> data) {
        List<Record> result = new LinkedList<Record>();
        for (Record record : data) {
            result.add(record.clone());
        }
        return result;
    }

    @Override
    public List<Record> priceFilter(List<Record> records, int priceOption, Double price) {
        if (priceOption == IGNORE) {
            return records;
        }

        List<Record> result = new LinkedList<Record>();

        for (Record record : records) {
            if ((priceOption == MORE_THAN && record.getPrice() >= price) ||
                    (priceOption == LESS_THAN && record.getPrice() <= price)) {
                result.add(record);
            }
        }

        return result;
    }

    @Override
    public List<Record> sortByPrice(List<Record> records, final boolean isAsc) {
        if (records == null || records.isEmpty()) {
            return records;
        }

        List<Record> result = new LinkedList<Record>(records);

        Collections.sort(result, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                int ascDesc = (isAsc) ? 1 : -1;
                return ascDesc*new Double(o1.getPrice()).compareTo(new Double(o2.getPrice()));
            }
        });

        return result;
    }

    @Override
    public List<Record> getUserCart(String userName) {   // TODO test me
        initUserCart(userName);

        return copy(userCard.get(userName)); // TODO test copying
    }

    private void initUserCart(String userName) {
        if (userCard.get(userName) == null) {
            userCard.put(userName, new LinkedList<Record>());
        }
    }

    @Override
    public void addToUserCart(String userName, List<Integer> recordIds) { // TODO test me
        initUserCart(userName);

        List<Record> records = getRecordsByIds(recordIds);

        userCard.get(userName).addAll(records);
    }

    private List<Record> getRecordsByIds(List<Integer> recordIds) {
        List<Record> result = new LinkedList<Record>();
        for (Integer id : recordIds) {
            Record record = findRecord(id);
            result.add(record.clone()); // TODO test copy
        }
        return result;
    }

    private Record findRecord(Integer id) {
        for (Record record : data) {
            if (record.itsMe(id)) {
                return record;
            }
        }
        return new RecordNotFound(); // TODO test me
    }



    private List<Record> foundTextAtDescription(List<Record> list, String foundString) {
        List<Record> result = new LinkedList<Record>();
        for (Record record : list) {
            if (record.getDescription().toLowerCase().contains(foundString.toLowerCase())) {
                result.add(record);
            }
        }
        return result;
    }
}
