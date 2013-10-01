/*
 * tab for injecting, deleting storlet from Object Service
 */
package eu.visioncloud.workflowWebUI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.FieldType;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import eu.visioncloud.cci.client.ClientInterface;
import eu.visioncloud.cci.client.ContentCentricException;
import eu.visioncloud.storlet.common.AuthToken;
import eu.visioncloud.storlet.common.ObjIdentifier;
import eu.visioncloud.storlet.common.SPEConstants;
import eu.visioncloud.storlet.common.StorletException;
import eu.visioncloud.storlet.common.Utils;
import eu.visioncloud.workflow.constants.WorkflowMngConst;
import eu.visioncloud.workflowengine.obj.HandlerInfo;

public class InjectTab extends HorizontalLayout {
	private VerticalLayout storletInfo;
	private ListSelect storletList;
	private VerticalLayout btnPanel;
	private ComboBox containersList;
	private TextField contField;

	private ClientInterface oClient;
	private String tenant;
	private AuthToken authToken;
	private String[] containers;
	private String currentContainer;
	private String[] storletNames;

	private NativeButton inject;
	private NativeButton remove;
	private NativeButton reload;

	private VerticalLayout controlWrapper;

	private UploadField instObj;
	private UploadField params;
	private UploadField constraints;
	private TextField slID;
	private TextField slDefID;
	private TextField slClassName;
	private TextArea triggers;
	private TextArea outputs;
	private CheckBox withDef;
	private UploadField defObj;
	private UploadField defParams;
	private UploadField defConstraints;
	private TextArea slDesc;

	private static final Logger logger = Logger.getLogger("workflowUIApp");

	public InjectTab(String t, AuthToken auth, ClientInterface client) {
		tenant = t;
		oClient = client;
		authToken = auth;
		this.containers = getContainers();
		if (containers != null) {
			this.currentContainer = containers[0];
		}
		buildList();
		buildControlPanel();
		buildStorletInfoPanel();
		this.setWidth("-1px");
		this.setHeight("100%");
	}

	private void buildStorletInfoPanel() {

		storletInfo = new VerticalLayout();
		storletInfo.setWidth("400px");
		storletInfo.setHeight("100%");
		slID = new TextField();
		slID.setCaption("Storlet Object ID");
		slID.setWidth("200px");
		storletInfo.addComponent(slID);
		slDefID = new TextField();
		slDefID.setCaption("Storlet Definition Object ID");
		slDefID.setWidth("200px");
		storletInfo.addComponent(slDefID);
		slClassName = new TextField();
		slClassName.setCaption("Storlet Class Name");
		slClassName.setWidth("200px");
		storletInfo.addComponent(slClassName);
		triggers = new TextArea();
		triggers.setCaption("Storlet Trigger Definition");
		
		triggers.setWidth("200px");
		storletInfo.addComponent(triggers);
		outputs = new TextArea();
		outputs.setCaption("Storlet Outputs Definition");
		outputs.setWidth("200px");
		storletInfo.addComponent(outputs);
		params = new UploadField();
		params.setFieldType(FieldType.FILE);
		params.setCaption("Storlet params File");
		params.setButtonCaption("Choose file...");
		params.setImmediate(true); // default, should not reset caption
		storletInfo.addComponent(params);
		constraints = new UploadField();
		constraints.setFieldType(FieldType.FILE);
		constraints.setCaption("Storlet constraints File");
		constraints.setButtonCaption("Choose file...");
		constraints.setImmediate(true); // default, should not reset caption
		storletInfo.addComponent(constraints);
		instObj = new UploadField();
		instObj.setFieldType(FieldType.FILE);
		instObj.setCaption("Storlet Instance Jar File");
		instObj.setButtonCaption("Choose file...");
		instObj.setImmediate(true); // default, should not reset caption
		storletInfo.addComponent(instObj);

		withDef = new CheckBox("With Template?");
		// /withDef.setCaption();
		withDef.setImmediate(true);
		withDef.addListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				defParams.setEnabled(withDef.booleanValue());
				defConstraints.setEnabled(withDef.booleanValue());
				defObj.setEnabled(withDef.booleanValue());
				slDesc.setEnabled(withDef.booleanValue());
			}

		});
		storletInfo.addComponent(withDef);
		defParams = new UploadField();
		defParams.setFieldType(FieldType.FILE);
		defParams.setCaption("Storlet Template Params File");
		defParams.setButtonCaption("Choose file...");
		defParams.setImmediate(true); // default, should not reset caption
		defParams.setEnabled(false);
		storletInfo.addComponent(defParams);
		defConstraints = new UploadField();
		defConstraints.setFieldType(FieldType.FILE);
		defConstraints.setCaption("Storlet Template Constraints File");
		defConstraints.setButtonCaption("Choose file...");
		defConstraints.setImmediate(true); // default, should not reset caption
		defConstraints.setEnabled(false);
		storletInfo.addComponent(defConstraints);
		defObj = new UploadField();
		defObj.setFieldType(FieldType.FILE);
		defObj.setCaption("Storlet Template Jar File");
		defObj.setButtonCaption("Choose file...");
		defObj.setImmediate(true); // default, should not reset caption
		defObj.setEnabled(false);
		storletInfo.addComponent(defObj);
		slDesc = new TextArea();
		slDesc.setCaption("Storlet Description");
		slDesc.setWidth("200px");
		slDesc.setEnabled(false);
		storletInfo.addComponent(slDesc);

		Panel storletPanel = new Panel();
		storletPanel.setHeight("100%");
		storletPanel.addComponent(storletInfo);
		this.addComponent(storletPanel);
	}

	private void buildList() {
		storletList = new ListSelect();
		storletList.setImmediate(false);
		storletList.setWidth("200px");
		storletList.setHeight("100.0%");
		storletList.setCaption("Storlet List");
		storletList.setMultiSelect(true);
		storletList.setNullSelectionAllowed(false);
		storletList.setStyleName("my-style");
		loadStorlets();

		Panel listWrapper = new Panel();
		listWrapper.setWidth("400px");
		listWrapper.setHeight("100%");
		listWrapper.setImmediate(false);
		//listWrapper.getContent().setSizeUndefined();
		listWrapper.addComponent(storletList);

		this.addComponent(listWrapper);
	}

	private void buildBtnPanel() {
		btnPanel = new VerticalLayout();
		btnPanel.setImmediate(false);
		btnPanel.setWidth("150px");
		btnPanel.setHeight("-1px");
		btnPanel.setMargin(false);
		btnPanel.setSpacing(true);

		// inject
		inject = new NativeButton();
		inject.setCaption("INJECT");
		inject.setImmediate(true);
		inject.setWidth("90px");
		inject.setHeight("30px");
		inject.addListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 941928594106974769L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				try {
					Properties slParams = new Properties();
					if (params.getValue() != null)
						slParams.load(new FileInputStream((File) params
								.getValue()));
					Properties slConstraints = new Properties();
					if (constraints.getValue() != null)
						slConstraints.load(new FileInputStream(
								(File) constraints.getValue()));
					byte[] slSrcContent;
					if (instObj.getValue() != null) {
						slSrcContent = Utils.fileToByteArray((File) instObj
								.getValue());
					} else
						slSrcContent = null;
					//System.out.println(authToken.getAuthenticationString());
					ByteArrayOutputStream slContentStream = Utils
							.createSLContent(slParams, slConstraints,
									slSrcContent,
									authToken.getAuthenticationString());
					Utils.injectStorlet(new ObjIdentifier(tenant,
							currentContainer, slDefID.getValue().toString()),
							new ObjIdentifier(tenant, currentContainer, slID
									.getValue().toString()), slContentStream
									.toByteArray(), slClassName.getValue()
									.toString(),
							triggers.getValue().toString(), authToken,
							WorkflowMngConst.ccsURL);
					oClient.setObjectMetadata(tenant, currentContainer, slID
									.getValue().toString(), "_slOutputs", outputs.getValue().toString());
					if (withDef.booleanValue() == true) {
						Properties slDefParams = new Properties();
						if (defParams.getValue() != null)
							slParams.load(new FileInputStream((File) defParams
									.getValue()));
						Properties slDefConstraints = new Properties();
						if (defConstraints.getValue() != null)
							slDefConstraints.load(new FileInputStream(
									(File) defConstraints.getValue()));
						byte[] slDefSrcContent;
						if (defObj.getValue() != null) {
							slDefSrcContent = Utils
									.fileToByteArray((File) defObj.getValue());
						} else
							slDefSrcContent = null;
						ByteArrayOutputStream slDefContentStream = Utils
								.createSLContent(slDefParams, slDefConstraints,
										slDefSrcContent,
										authToken.getAuthenticationString());
						Utils.injectStorletDefinition(
								new ObjIdentifier(tenant, currentContainer,
										slDefID.getValue().toString()),
								slDefContentStream.toByteArray(), slDesc
										.getValue().toString(), authToken,
								WorkflowMngConst.ccsURL);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.info("IO exception ", e);
				} catch (ContentCentricException e) {
					// TODO Auto-generated catch block
					logger.info("CCS exception ", e);
				} catch (StorletException e) {
					// TODO Auto-generated catch block
					logger.info("Storlet exception ", e);
				}
			}

		});
		btnPanel.addComponent(inject);
		btnPanel.setComponentAlignment(inject, Alignment.MIDDLE_CENTER);
		// remove
		remove = new NativeButton();
		remove.setCaption("REMOVE");
		remove.setImmediate(true);
		remove.setWidth("90px");
		remove.setHeight("30px");
		remove.addListener(new Button.ClickListener() {
			/**
					 * 
					 */
			private static final long serialVersionUID = -8909864437538546744L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					if (storletList.getValue() != null) {
						Set<Object> selectedIds = (Set<Object>) storletList
								.getValue();
						for (Object o : selectedIds) {
							oClient.deleteObject(tenant, currentContainer,
									o.toString());
							storletList.removeItem(o);
						}
					}
				} catch (ContentCentricException e) {
					// TODO Auto-generated catch block
					logger.info("CCS exception ", e);
				}
			}

		});
		btnPanel.addComponent(remove);
		btnPanel.setComponentAlignment(remove, Alignment.MIDDLE_CENTER);
		// reload
		reload = new NativeButton();
		reload.setCaption("RELOAD");
		reload.setImmediate(true);
		reload.setWidth("90px");
		reload.setHeight("30px");
		reload.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (contField.getValue().toString().equals("")
						&& containersList.getValue() != null)
					currentContainer = containersList.getValue().toString();
				else
					currentContainer = contField.getValue().toString();
				if (!currentContainer.equals("")) {
					loadStorlets();
				}
			}
		});
		btnPanel.addComponent(reload);
		btnPanel.setComponentAlignment(reload, Alignment.MIDDLE_CENTER);

		controlWrapper.addComponent(btnPanel);
		controlWrapper.setComponentAlignment(btnPanel, Alignment.MIDDLE_CENTER);
	}

	private void buildContainerField() {
		containersList = new ComboBox();
		containersList.setCaption("Valid Containers");
		containersList.setWidth("-1px");
		containersList.setNullSelectionAllowed(false);
		fillContainersList();
		containersList.setValue(currentContainer);

		contField = new TextField();
		contField.setCaption("Custom Container");
		contField.setWidth("-1px");
		contField.setValue("");

		controlWrapper.addComponent(containersList);
		controlWrapper.setComponentAlignment(containersList,
				Alignment.MIDDLE_CENTER);
		controlWrapper.addComponent(contField);
		controlWrapper
				.setComponentAlignment(contField, Alignment.MIDDLE_CENTER);
	}

	private void buildControlPanel() {
		controlWrapper = new VerticalLayout();
		controlWrapper.setSizeUndefined();
		controlWrapper.setImmediate(false);
		controlWrapper.setSpacing(true);
		// controlWrapper.getContent().setSizeUndefined();
		buildContainerField();
		buildBtnPanel();

		this.addComponent(controlWrapper);
		this.setComponentAlignment(controlWrapper, Alignment.MIDDLE_CENTER);
	}

	private void loadStorlets() {
		try {
			storletNames = oClient.getObjectIdsForMetadata(tenant,
					currentContainer, SPEConstants.STORLET_TAG_TRIGGERS);
			storletList.removeAllItems();
			if (storletNames.length > 0) {
				for (String name : storletNames) {
					storletList.addItem(name);
				}
			}
		} catch (ContentCentricException e) {
			// TODO Auto-generated catch block
			logger.info("", e);
		}
	}

	private String[] getContainers() {
		try {
			String[] containers = oClient.listContainers(tenant);
			return containers;
		} catch (ContentCentricException e) {
			// TODO Auto-generated catch block
			logger.error("Load containers failed ", e);
			// tenant = WorkflowMngConst.tenant;
			return null;
		}
	}

	private void fillContainersList() {
		if (containers != null) {
			for (String container : containers) {
				containersList.addItem(container);
			}
		}
	}
	
	private String noEscape(String str){
		return str.replaceAll("\\\"", "\"").replaceAll("\\\\", "\\");
	}

}
