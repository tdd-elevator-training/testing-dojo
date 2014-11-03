package web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

public class ServerRunner {

    private static ServerRunner instance = new ServerRunner();

    private Server server;

    private ServerRunner() {
    }

    public static ServerRunner getInstance() {
        return instance;
    }
    public String getProjectRoot() throws URISyntaxException {
        URL u = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        File f = new File(u.toURI());
        return f.getParent();
    }

    public int start(String contextPath) throws Exception {
        stop();

        server = new Server(0);

        server.setHandler(loadWebContext(contextPath));
        server.start();
        return server.getConnectors()[0].getLocalPort();
    }

    /**
     * Метод моздан для того, чтобы найти контекст - дело в том, тесты можно запкускать из проекта dojo-server и из
     * коневого проекта testing-dojo - и тогда нужны разные пути. Я пока не определился как задать это единоразово,
     * но разберусь TODO
     * @return загруженный вебконтекст
     */
    private WebAppContext loadWebContext(String contextPath) throws IOException {
        Collection<String> urls = Arrays.asList(new String[]{"src/main/webapp", "dojo-server/src/main/webapp"});
        for (String url : urls) {
            WebAppContext context = new WebAppContext(url, contextPath);
            Resource resource = context.newResource(context.getWar());
            if (resource.exists()) {
                return context;
            }
        }
        throw new RuntimeException("Webapp not found!");
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