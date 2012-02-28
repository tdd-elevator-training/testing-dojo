package org.automation.dojo.web;

public final class JettyServerRunner {

	public static void main(String[] args) throws Exception {
		int port = ServerRunner.getInstance().start();
		System.out.println("http://localhost:" + port + "/Shop");
		ServerRunner.getInstance().join();
	}

}
