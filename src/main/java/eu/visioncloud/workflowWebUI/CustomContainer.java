package eu.visioncloud.workflowWebUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import eu.visioncloud.workflowengine.obj.HandlerInfo;

public class CustomContainer extends Window {
	private VerticalLayout vPanel = new VerticalLayout();
	private TextField contField = new TextField();
	private Button btnModi = new Button("OK");
	private String container;
	
	public CustomContainer(){
		initPanel();
		contField.setValue("");
	}
	
	private void initPanel(){
		this.setCaption("Handler Form");
		this.setHeight("-1px");
		this.setWidth("250px");
		vPanel.setHeight("-1px");
		vPanel.setWidth("-1px");
		vPanel.addComponent(contField);
		contField.setCaption("Container Name: ");
		contField.setHeight("-1px");
		contField.setWidth("-1px");
		vPanel.addComponent(btnModi);
		btnModi.setHeight("-1px");
		btnModi.setWidth("-1px");
		btnModi.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				setContainer(contField.getValue().toString());
				close();
			}
		});
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}
}
