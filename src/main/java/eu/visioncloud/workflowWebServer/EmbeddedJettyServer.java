package eu.visioncloud.workflowWebServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import eu.visioncloud.workflow.constants.WorkflowMngConst;

public class EmbeddedJettyServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmbeddedJettyServer emServer = new EmbeddedJettyServer();
		emServer.setup(WorkflowMngConst.ip, Integer.parseInt(WorkflowMngConst.port),
				Integer.parseInt(WorkflowMngConst.threads));
		try {
			emServer.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	Server server;
	public void setup(String ip, int port, int threadNum) {
		server = new Server();
		WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/workflowmanager");
        //webAppContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        //webAppContext.setResourceBase("src/main/webapp");
        webAppContext.setDescriptor("webapp/WEB-INF/web.xml");
        webAppContext.setResourceBase("webapp");
      
        webAppContext.setParentLoaderPriority(true);
        server.setHandler(webAppContext);
        

        SelectChannelConnector connector1 = new SelectChannelConnector();
        connector1.setHost(ip);
        connector1.setPort(port);
        connector1.setThreadPool(new QueuedThreadPool(threadNum));
        server.addConnector(connector1);
	}

	public void run() throws Exception {
		server.start();
		server.join();
	}
}
