package eu.visioncloud.workflowWebUI;

import obj.HandlerInfo;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class HandlerForm extends Window{
	private VerticalLayout vPanel = new VerticalLayout();
	private TextField nameField = new TextField();
	private TextField inputField = new TextField();
	private TextField outputField = new TextField();
	private Button btnModi = new Button("Modify");
	private HandlerInfo handlerInfo = null;
	public HandlerForm() {
		this.setCaption("New Handler");
		this.setHeight("-1px");
		this.setWidth("-1px");
		initPanel();
		nameField.setValue("Handler1");
		inputField.setValue("default");
		outputField.setValue("default");
		this.addComponent(vPanel);
	}
	public HandlerForm(HandlerInfo hInfo) {
		this.setHeight("-1px");
		this.setWidth("-1px");
		setHandlerInfo(hInfo);
		initPanel();
		nameField.setValue(hInfo.getName());
		inputField.setValue(hInfo.getInputExpr());
		outputField.setValue(hInfo.getOutputExpr());
		this.addComponent(vPanel);
		// TODO Auto-generated constructor stub
	}
	
	private void initPanel(){
		
		vPanel.setHeight("-1px");
		vPanel.setWidth("-1px");
		vPanel.addComponent(nameField);
		nameField.setCaption("Handler Name: ");
		nameField.setHeight("-1px");
		nameField.setWidth("-1px");
		vPanel.addComponent(inputField);
		inputField.setCaption("Input: ");
		inputField.setHeight("-1px");
		inputField.setWidth("-1px");
		vPanel.addComponent(outputField);
		outputField.setCaption("Output: ");
		outputField.setHeight("-1px");
		outputField.setWidth("-1px");
		vPanel.addComponent(btnModi);
		btnModi.setHeight("-1px");
		btnModi.setWidth("-1px");
		btnModi.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				setHandlerInfo(new HandlerInfo(nameField.getValue().toString(), inputField.getValue().toString(),
						outputField.getValue().toString()));
			}
		});
	}

	public HandlerInfo getHandlerInfo() {
		return handlerInfo;
	}

	public void setHandlerInfo(HandlerInfo handlerInfo) {
		this.handlerInfo = handlerInfo;
	}

}
