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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import eu.visioncloud.storlet.common.StorletException;
import eu.visioncloud.storlet.common.Trigger;
import eu.visioncloud.storlet.common.Utils;
import eu.visioncloud.workflowengine.matcher.TriggerMatcher;
import eu.visioncloud.workflowengine.obj.HandlerInfo;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {
	private Window window;
	private MainComponent mainComponent;

	private Set<HandlerInfo> handlers;

	@Override
	public void init() {
		window = new Window("My Vaadin Application");

		setMainWindow(window);

		try {
			handlers = LoadHandlers();
		} catch (IOException | StorletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mainComponent = new MainComponent(handlers);
		window.setContent(mainComponent);
		setTheme("mytheme");

	}

	private Set<HandlerInfo> LoadHandlers() throws IOException, StorletException{ //for debugging, change to load from NS later, update when client loads everytime

		Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
		//for (int i = 1; i <= num; ++i){
			//br = new BufferedReader(new FileReader("triggers/2.txt"));
			//System.out.println("OK till here" + num);
			//while ((handler = br.readLine()) != null){
		RandomAccessFile in = new RandomAccessFile("triggers/2.txt","r");
		byte[] buff = new byte[(int) in.length()];
		in.readFully(buff);
		String value = new String(buff);
		System.out.println(Utils.jsonUnEscape(value));
				Trigger[] triggers = Trigger.createTriggers(value);
				for (Trigger trigger : triggers){
				//System.out.println("Trigger: " + trigger.getTriggerEvaluator());
				System.out.println("Output: " + trigger.getOutputEvaluator());
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
}
