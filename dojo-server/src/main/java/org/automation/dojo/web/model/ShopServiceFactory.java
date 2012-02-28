package org.automation.dojo.web.model;

public class ShopServiceFactory {

    public static ShopService gtInstance() {
        return new ShopServiceInMemory();
    }

}
