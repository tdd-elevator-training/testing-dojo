package org.automation.dojo.web;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class FunctionalTestCase {

	protected static WebTester tester;

	@BeforeClass
	public static void init() throws Exception {
	    int port = ServerRunner.getInstance().start();        
	    
	    tester = new WebTester();
	    tester.getTestContext().setBaseUrl(
	            "http://localhost:" + port + "/Shop");

        tester.beginAt("/");
	}

	@AfterClass
	public static void end() throws Exception {
		ServerRunner.getInstance().stop();
	}

	public FunctionalTestCase() {
		super();
	}

}