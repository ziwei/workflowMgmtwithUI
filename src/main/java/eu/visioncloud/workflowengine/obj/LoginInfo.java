package eu.visioncloud.workflowengine.obj;

public class LoginInfo {
	public String tenant;
	public String userId;
	public String password;
	
	public LoginInfo(String t, String u, String p){
		tenant = t;
		userId = u;
		password = p;
	}
}
