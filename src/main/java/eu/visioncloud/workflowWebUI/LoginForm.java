package eu.visioncloud.workflowWebUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import eu.visioncloud.workflowengine.obj.HandlerInfo;
import eu.visioncloud.workflowengine.obj.LoginInfo;

public class LoginForm extends Window {
	private VerticalLayout vPanel = new VerticalLayout();
	private TextField tenField = new TextField();
	private TextField userField = new TextField();
	private TextField pwField = new TextField();
	private Button btnLogin = new Button("Login");
	private LoginInfo loginInfo;

	public LoginForm() {

		initPanel();
		tenField.setValue("");
		userField.setValue("");
		pwField.setValue("");
		this.addComponent(vPanel);
	}

	private void initPanel() {
		this.setCaption("Login Form");
		this.setHeight("-1px");
		this.setWidth("250px");
		vPanel.setHeight("-1px");
		vPanel.setWidth("-1px");
		vPanel.addComponent(tenField);
		tenField.setCaption("Tenant: ");
		tenField.setHeight("-1px");
		tenField.setWidth("-1px");
		vPanel.addComponent(userField);
		userField.setCaption("User ID: ");
		userField.setHeight("-1px");
		userField.setWidth("-1px");
		vPanel.addComponent(pwField);
		pwField.setCaption("Password: ");
		pwField.setHeight("-1px");
		pwField.setWidth("-1px");
		vPanel.addComponent(btnLogin);
		btnLogin.setHeight("-1px");
		btnLogin.setWidth("-1px");
		btnLogin.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				setLoginInfo(new LoginInfo(tenField.getValue().toString(),
						userField.getValue().toString(), pwField
								.getValue().toString()));
				close();
			}
		});
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
}
