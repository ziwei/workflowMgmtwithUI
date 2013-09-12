package eu.visioncloud.workflowWebUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import eu.visioncloud.workflowengine.obj.HandlerInfo;

public class HandlerForm extends Window {
	private VerticalLayout vPanel = new VerticalLayout();
	private TextField slField = new TextField();
	private TextField nameField = new TextField();
	private TextField triggerField = new TextField();
	private TextField outputField = new TextField();
	private Button btnModi = new Button("Save");
	private HandlerInfo handlerInfo = null;

	public HandlerForm() {

		initPanel();
		slField.setValue("");
		nameField.setValue("");
		triggerField.setValue("");
		outputField.setValue("");
		this.addComponent(vPanel);
	}

	public HandlerForm(HandlerInfo hInfo) {
		setHandlerInfo(hInfo);
		initPanel();
		slField.setValue(hInfo.getSlName());
		nameField.setValue(hInfo.getName());
		triggerField.setValue(hInfo.getInputExpr());
		outputField.setValue(hInfo.getOutputExpr());
		this.addComponent(vPanel);
		// TODO Auto-generated constructor stub
	}

	private void initPanel() {
		this.setCaption("Handler Form");
		this.setHeight("-1px");
		this.setWidth("250px");
		vPanel.setHeight("-1px");
		vPanel.setWidth("-1px");
		vPanel.addComponent(slField);
		slField.setCaption("Handler Name: ");
		slField.setHeight("-1px");
		slField.setWidth("-1px");
		vPanel.addComponent(nameField);
		nameField.setCaption("Handler Name: ");
		nameField.setHeight("-1px");
		nameField.setWidth("-1px");
		vPanel.addComponent(triggerField);
		triggerField.setCaption("trigger: ");
		triggerField.setHeight("-1px");
		triggerField.setWidth("-1px");
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
				setHandlerInfo(new HandlerInfo(slField.getValue().toString(),
						nameField.getValue().toString(), triggerField
								.getValue().toString(), outputField.getValue()
								.toString(), true));
				close();
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
