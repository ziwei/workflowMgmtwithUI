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
import java.util.HashSet;
import java.util.Set;

import matcher.TriggerMatcher;

import obj.HandlerInfo;


import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application
{
    private Window window;
    private MainComponent mainComponent;
    
    private Set<HandlerInfo> handlers;

    @Override
    public void init()
    {
        window = new Window("My Vaadin Application");
        
        setMainWindow(window);
        
        try {
			handlers = LoadHandlers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mainComponent = new MainComponent(handlers);
        window.setContent(mainComponent);
        
    }
    private Set<HandlerInfo> LoadHandlers() throws IOException{ //for debugging, change to load from NS later, update when client loads everytime
		BufferedReader br;
		String name;
		String inputExpr;
		String outputExpr;
		Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
		//for (int i = 1; i <= num; ++i){
			br = new BufferedReader(new FileReader("/home/ziwei/workspace/TriggerMatcher/test/1.txt"));
			//System.out.println("OK till here" + num);
			while ((name = br.readLine()) != null){
				inputExpr = br.readLine();
				outputExpr = br.readLine();
				handlers.add(new HandlerInfo(name, inputExpr, outputExpr));
				br.readLine();
			}
			//System.out.println("OK till here");
			br.close();
		
		//}
		//handlers.add(new HandlerInfo("ImageConverting.handler1", "'('\"FileType\"=\"'Image'\"')'&&'('\"Format\"~\"'JPEG|BMP|GIF'\"')'", "'('\"Format\"=\"'PNG'\"')'"));
		//handlers.add(new HandlerInfo("TextConverting.handler1", "'('\"FileType\"=\"'Text'\"')'&&'('\"Format\"~\"'txt|doc|odt'\"')'", "'('\"Format\"=\"'pdf'\"')'"));
		return handlers;
	}
}
