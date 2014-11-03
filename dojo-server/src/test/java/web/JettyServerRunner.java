package web;

public final class JettyServerRunner {

    public static void main(String[] args) throws Exception {
        String contextPath = "/at-dojo";
        int port = ServerRunner.getInstance().start(contextPath);
        System.out.println("http://localhost:" + port + contextPath);
        ServerRunner.getInstance().join();
    }

}
