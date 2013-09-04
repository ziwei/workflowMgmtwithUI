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

import com.vaadin.Application;
import com.vaadin.ui.Window;

import eu.visioncloud.cci.client.*;
import eu.visioncloud.workflowengine.obj.Trigger;
import eu.visioncloud.workflowengine.obj.HandlerInfo;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {
	private Window window;
	private MainComponent mainComponent;
	private static ClientInterface oClient;
	private Set<HandlerInfo> handlers;
	static {
		String storageServiceUrl = "";
		oClient = new CdmiRestClient(storageServiceUrl);
	}
	@Override
	public void init() {
		window = new Window("Workflow Management Service");

		setMainWindow(window);

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

	private Set<HandlerInfo> LoadLocalHandlers() throws IOException{ //for debugging, change to load from NS later, update when client loads everytime

		Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
		//for (int i = 1; i <= num; ++i){
			//br = new BufferedReader(new FileReader("triggers/2.txt"));
			//System.out.println("OK till here" + num);
			//while ((handler = br.readLine()) != null){
		RandomAccessFile in = new RandomAccessFile("triggers/2.txt","r");
		byte[] buff = new byte[(int) in.length()];
		in.readFully(buff);
		String value = new String(buff);
		System.out.println(Trigger.jsonUnEscape(value));
				Trigger[] triggers = Trigger.createTriggers(value);
				for (Trigger trigger : triggers){
				//System.out.println("Trigger: " + trigger.getTriggerEvaluator());
				//System.out.println("Output: " + trigger.getOutputEvaluator());
				handlers.add(new HandlerInfo(trigger.getHandlerID(), trigger.getTriggerEvaluator(), trigger.getOutputEvaluator()));
				//}
				//br.readLine();
			}
			//System.out.println("OK till here");
			//br.close();
		
		//}
		//handlers.add(new HandlerInfo("ImageConverting.handler1", "'('\"FileType\"=\"'Image'\"')'&&'('\"Format\"~\"'JPEG|BMP|GIF'\"')'", "'('\"Format\"=\"'PNG'\"')'"));
		//handlers.add(new HandlerInfo("TextConverting.handler1", "'('\"FileType\"=\"'Text'\"')'&&'('\"Format\"~\"'txt|doc|odt'\"')'", "'('\"Format\"=\"'pdf'\"')'"));
		return handlers;
	}
	
	private Set<HandlerInfo> LoadHandlers() throws ContentCentricException {
		Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
		oClient.queryEquivalentAware("equivalenceId", "objId", "tenId", "contId"); //how to use it for query?
		return handlers;
	}
}
