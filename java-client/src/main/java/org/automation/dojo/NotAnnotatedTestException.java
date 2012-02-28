package org.automation.dojo;

public class NotAnnotatedTestException extends RuntimeException {

    public NotAnnotatedTestException(String methodName) {
        super("Test method ["+methodName+"] is not assigned with testing scenario. \n" +
                "Should be @" + Scenario.class.getSimpleName() + " annotation on the test method. \n" +
                "No results will be reported to server!");
    }
}
