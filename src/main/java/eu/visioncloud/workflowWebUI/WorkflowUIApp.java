/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package eu.visioncloud.workflowWebUI;

import org.apache.log4j.Logger;
import com.vaadin.Application;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

import eu.visioncloud.cci.client.*;
import eu.visioncloud.storlet.common.AuthToken;
import eu.visioncloud.workflow.constants.WorkflowMngConst;
import eu.visioncloud.workflowengine.obj.LoginInfo;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class WorkflowUIApp extends Application {
	private Window window;
	private MainComponent mainComponent;
	private ClientInterface oClient;

	final static String storageServiceUrl = WorkflowMngConst.ccsURL;
	private String tenant = "";
	private String userId = "";
	private String password = "";

	private AuthToken auth;

	private static final Logger logger = Logger.getLogger("workflowUIApp");

	@Override
	public void init() {
		window = new Window("Workflow Management Service");

		setMainWindow(window);
		final LoginForm lf = new LoginForm();
		lf.setClosable(false);
		lf.center();
		lf.addListener(new Window.CloseListener() {

			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				LoginInfo login = lf.getLoginInfo();
				if (login != null) {
					tenant = login.tenant;
					userId = login.userId;
					password = login.password;
					auth = initCCIClient();
					if (oClient != null) {
						mainComponent = new MainComponent(tenant, auth, oClient);
						window.setContent(mainComponent);
					} else {
						lf.showNotification("Invalid tenant name, user id or password");
						lf.getApplication().close();
					}
				}
			}
		});
		window.addWindow(lf);
		setTheme("mytheme");
	}

	private AuthToken initCCIClient() {
		if (userId != "" && tenant != "" && password != "") {
			AuthToken authToken = new AuthToken(tenant, userId, password);
			oClient = new CdmiRestClient(storageServiceUrl,
					authToken.getAuthenticationString());
			return authToken;
		} else {
			// System.out.println("back door");
			oClient = new CdmiRestClient(storageServiceUrl);
			return new AuthToken(tenant, "", "");
		}
	}

}
