package eu.visioncloud.workflowWebServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class EmbeddedJettyServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmbeddedJettyServer emServer = new EmbeddedJettyServer();
		emServer.setup(8080);
		try {
			emServer.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	Server server;
	public void setup(int port) {
		server = new Server(port);
		WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/workflowWebUI");
        webAppContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        webAppContext.setResourceBase("src/main/webapp");
        webAppContext.setParentLoaderPriority(true);
        server.setHandler(webAppContext);
	}

	public void run() throws Exception {
		server.start();
		server.join();
	}
}
