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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONObject;

import com.vaadin.Application;
import com.vaadin.ui.Window;

import eu.visioncloud.cci.client.*;
import eu.visioncloud.storlet.common.AuthToken;
import eu.visioncloud.storlet.common.SPEConstants;
import eu.visioncloud.workflow.constants.WorkflowMngConst;
import eu.visioncloud.workflowengine.obj.Trigger;
import eu.visioncloud.workflowengine.obj.HandlerInfo;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class WorkflowUIApp extends Application {
	private Window window;
	private MainComponent mainComponent;
	private static ClientInterface oClient;
	private Set<HandlerInfo> handlers;

	private final static String storageServiceUrl = WorkflowMngConst.ccsURL;
	private final static String tenant = WorkflowMngConst.tenant;
	private final static String container = WorkflowMngConst.container;
	private final static String user = WorkflowMngConst.user;
	private final static String password = WorkflowMngConst.password;
	static {

		if (user != null && tenant != null && password != null) {
			AuthToken authToken = new AuthToken(user, tenant, password);
			oClient = new CdmiRestClient(storageServiceUrl,
					authToken.getAuthenticationString());
		} else {
			oClient = new CdmiRestClient(storageServiceUrl);
		}
	}

	@Override
	public void init() {
		window = new Window("Workflow Management Service");

		setMainWindow(window);

		//handlers = LoadHandlers();
		try {
			handlers = LoadLocalHandlers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainComponent = new MainComponent(handlers);
		window.setContent(mainComponent);
		setTheme("mytheme");

	}

	private Set<HandlerInfo> LoadHandlers() {
		Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
		try {
			// String[] objList = oClient.getContainerContents("TestTenant",
			// "edt-testChain6");
			String[] slList = oClient.getObjectIdsForMetadata(tenant,
					container, SPEConstants.STORLET_TAG_TRIGGERS);
			for (String slName : slList) {
				// if (oClient.getObjectMetadataEntry("TestTenant",
				// "edt-testChain6", slName, "output") != null) {
				String outputs = oClient.getObjectMetadataEntry(tenant,
						container, slName, "_slOutputs");
				Trigger[] triggers = Trigger.createTriggers(oClient
						.getObjectMetadataEntry(tenant, container, slName,
								SPEConstants.STORLET_TAG_TRIGGERS));

				if (triggers != null) {
					for (Trigger trigger : triggers) {
						if (outputs != null) {
							JSONObject obj = JSONObject.fromObject(outputs);
							if (obj.containsKey(trigger.getHandlerID())) {
								handlers.add(new HandlerInfo(trigger
										.getHandlerID(), trigger
										.getTriggerEvaluator(), obj
										.getString(trigger.getHandlerID())));
							} else {
								handlers.add(new HandlerInfo(trigger
										.getHandlerID(), trigger
										.getTriggerEvaluator(), "oNA"));
							}
						} else {
							handlers.add(new HandlerInfo(
									trigger.getHandlerID(), trigger
											.getTriggerEvaluator(), "oNA"));
						}
					}
				}

				// }
			}
		} catch (ContentCentricException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handlers;
	}

	 private Set<HandlerInfo> LoadLocalHandlers() throws IOException {
	
	 Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
	 // for (int i = 1; i <= num; ++i){
	 // br = new BufferedReader(new FileReader("triggers/2.txt"));
	 // System.out.println("OK till here" + num);
	 // while ((handler = br.readLine()) != null){
	 RandomAccessFile in = new RandomAccessFile("triggers/2.txt", "r");
	 byte[] buff = new byte[(int) in.length()];
	 in.readFully(buff);
	 String value = new String(buff);
	 Trigger.jsonUnEscape(value);
	
	 // outputs
	 RandomAccessFile in1 = new RandomAccessFile("triggers/3.txt", "r");
	 byte[] buff1 = new byte[(int) in1.length()];
	 in1.readFully(buff1);
	 String value1 = new String(buff1);
	 Trigger.jsonUnEscape(value1);
	 JSONObject obj = JSONObject.fromObject(value1);
	
	 Trigger[] triggers = Trigger.createTriggers(value);
	 for (Trigger trigger : triggers) {
	 // System.out.println("Trigger: " + trigger.getTriggerEvaluator());
	 // System.out.println("Output: " + trigger.getOutputEvaluator());
	 if (obj.containsKey(trigger.getHandlerID()))
	 handlers.add(new HandlerInfo(trigger.getHandlerID(), trigger
	 .getTriggerEvaluator(), obj.getString(trigger
	 .getHandlerID())));
	 else
	 handlers.add(new HandlerInfo(trigger.getHandlerID(), trigger
	 .getTriggerEvaluator(), "oN\\/A"));
	 // }
	 // br.readLine();
	 }
	 // System.out.println("OK till here");
	 // br.close();
	
	 // }
	 // handlers.add(new HandlerInfo("ImageConverting.handler1",
	 // "'('\"FileType\"=\"'Image'\"')'&&'('\"Format\"~\"'JPEG|BMP|GIF'\"')'",
	 // "'('\"Format\"=\"'PNG'\"')'"));
	 // handlers.add(new HandlerInfo("TextConverting.handler1",
	 // "'('\"FileType\"=\"'Text'\"')'&&'('\"Format\"~\"'txt|doc|odt'\"')'",
	 // "'('\"Format\"=\"'pdf'\"')'"));
	 return handlers;
	 }
}
