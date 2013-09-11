package eu.visioncloud.workflow.constants;

import java.io.IOException;
import java.util.Properties;

public class WorkflowMngConst {
	private static Properties prop = new Properties();
	//private static Properties logProp = new Properties();

	static {
		try {
			prop.load(WorkflowMngConst.class.getResourceAsStream("/visioncloud_workflow.conf"));
			//logProp.load(SREConst.class.getResourceAsStream("/log4j.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// SRE constants
	public static final String tmpFilePath = prop
			.getProperty("tmpFilePath");
	public static final String logFilePath = prop.getProperty("logFilePath");
	public static final String dotPath = prop.getProperty("dotInstallPath");
	
	public static final String ccsURL = prop.getProperty("ccsURL");
	public static final String objsURL = prop.getProperty("objsURL");
	
	public static final String container = prop.getProperty("container");
	public static final String user = prop.getProperty("user");
	public static final String tenant = prop.getProperty("tenant");
	public static final String password = prop.getProperty("password");
	

	
	public static final String ip = prop.getProperty("ip");
	public static final String port = prop.getProperty("port");
	public static final String timeout = prop.getProperty("timeout");
	public static final String threads = prop.getProperty("threads");

}
