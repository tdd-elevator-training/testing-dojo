package org.automation.dojo.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerRunner {

	private static ServerRunner instance = new ServerRunner();
	
	private Server server;
	
	private ServerRunner() {		
	}
	
	public static ServerRunner getInstance() {
		return instance;
	}
	
	public int start() throws Exception {
		stop();
		
		server = new Server(0);
	    server.setHandler(new WebAppContext("src/main/webapp", "/Shop"));
	    server.start();
	    return server.getConnectors()[0].getLocalPort();
	}

	public void stop() throws Exception {
		if (server != null) {
			server.stop();
			server = null;
		}
	}
	
	public void join() throws Exception {
		if (server != null) {
			server.join();
		}
	}
}