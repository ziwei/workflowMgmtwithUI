package eu.visioncloud.workflowWebUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import eu.visioncloud.cci.client.ClientInterface;
import eu.visioncloud.cci.client.ContentCentricException;
import eu.visioncloud.storlet.common.SPEConstants;
import eu.visioncloud.workflow.constants.WorkflowMngConst;
import eu.visioncloud.workflowengine.matcher.TriggerMatcher;
import eu.visioncloud.workflowengine.obj.HandlerInfo;
import eu.visioncloud.workflowengine.obj.Trigger;

public class MainComponent extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private TabSheet mainTab;
	@AutoGenerated
	private HorizontalLayout hl_inputTab;
	@AutoGenerated
	private VerticalLayout btnPanel2;
	@AutoGenerated
	private NativeButton btn_genAbs;
	@AutoGenerated
	private Tree targetTree;
	@AutoGenerated
	private VerticalLayout btnPanel1;
	@AutoGenerated
	private NativeButton edit;
	@AutoGenerated
	private NativeButton create;
	@AutoGenerated
	private NativeButton remove_all;
	@AutoGenerated
	private NativeButton remove;
	@AutoGenerated
	private NativeButton add_all;
	@AutoGenerated
	private NativeButton add;
	private NativeButton reload;

	private ComboBox containersList;
	private TextField contField;
	@AutoGenerated
	private Tree sourceTree;

	private Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
	// private Set<HandlerInfo> selectedHandlers = new HashSet<HandlerInfo>();
	private byte[] imageByteArray;
	private static final Object CAPTION_PROPERTY = "caption";
	/**
	 * 
	 */
	private ClientInterface oClient;
	private String tenant;
	private String currentContainer = "";

	private String[] containers;

	private static final Logger logger = Logger.getLogger("workflowUIApp");
	private static final long serialVersionUID = 5324959476063726521L;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public MainComponent(String t, ClientInterface oClient) {
		this.oClient = oClient;
		this.tenant = t;
		this.containers = getContainers();
		if (containers != null) {
			this.currentContainer = containers[0];
		}
		if (!currentContainer.equals("")){
			this.handlers = LoadHandlers();
		}
		
		buildMainLayout();
		setCompositionRoot(mainLayout);
		// TODO add user code here
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// mainTab
		mainTab = buildMainTab();
		// mainLayout.addComponent(new Label("WTF"));
		mainLayout.addComponent(mainTab);

		Button logout = new Button("logout");
		logout.setStyleName("link");
		logout.setCaption("Logout from tenant " + this.tenant);
		logout.addListener(new Button.ClickListener() {
			public void buttonClick(Button.ClickEvent event) {
				getApplication().close();
			}
		});
		mainLayout.addComponent(logout, "right: 20px; top: 10px;");

		return mainLayout;
	}

	@AutoGenerated
	private TabSheet buildMainTab() {
		// common part: create layout
		mainTab = new TabSheet();
		mainTab.setImmediate(true);
		mainTab.setWidth("100.0%");
		mainTab.setHeight("100.0%");

		// hl_inputTab
		hl_inputTab = buildHl_inputTab();
		mainTab.addTab(hl_inputTab, "Config", null).setClosable(false);

		return mainTab;
	}

	@AutoGenerated
	private HorizontalLayout buildHl_inputTab() {
		// common part: create layout
		hl_inputTab = new HorizontalLayout();
		hl_inputTab.setImmediate(false);
		hl_inputTab.setWidth("-1px");
		hl_inputTab.setHeight("100.0%");
		hl_inputTab.setMargin(false);

		// sourceTree
		sourceTree = new Tree();
		sourceTree.setImmediate(false);
		sourceTree.setSizeUndefined();
		// sourceTree.setHeight("100.0%");
		sourceTree.setCaption("Source Handler List");
		sourceTree.addContainerProperty(CAPTION_PROPERTY, String.class, "");
		sourceTree
				.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
		sourceTree.setItemCaptionPropertyId(CAPTION_PROPERTY);
		sourceTree.setMultiSelect(true);
		sourceTree.setStyleName("my-style");
		fillTree();

		Panel sourceTreeWrapper = new Panel();
		sourceTreeWrapper.setWidth("400px");
		sourceTreeWrapper.setHeight("100%");
		sourceTreeWrapper.setImmediate(false);
		sourceTreeWrapper.getContent().setSizeUndefined();
		sourceTreeWrapper.addComponent(sourceTree);
		hl_inputTab.addComponent(sourceTreeWrapper);

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
		// btnPanel1
		btnPanel1 = buildBtnPanel1();
		// hl_inputTab.addComponent(btnPanel1);
		// hl_inputTab.setComponentAlignment(btnPanel1,
		// Alignment.MIDDLE_CENTER);
		VerticalLayout controlWrapper = new VerticalLayout();
		controlWrapper.setSizeUndefined();
		controlWrapper.setImmediate(false);
		controlWrapper.setSpacing(true);
		// controlWrapper.getContent().setSizeUndefined();
		controlWrapper.addComponent(containersList);
		controlWrapper.setComponentAlignment(containersList,
				Alignment.MIDDLE_CENTER);
		controlWrapper.addComponent(contField);
		controlWrapper
				.setComponentAlignment(contField, Alignment.MIDDLE_CENTER);
		controlWrapper.addComponent(btnPanel1);
		controlWrapper
				.setComponentAlignment(btnPanel1, Alignment.MIDDLE_CENTER);
		hl_inputTab.addComponent(controlWrapper);
		hl_inputTab.setComponentAlignment(controlWrapper,
				Alignment.MIDDLE_CENTER);
		// targetTree
		targetTree = new Tree();
		targetTree.setImmediate(false);
		targetTree.setSizeUndefined();
		targetTree.setCaption("Target Handler List");
		targetTree.addContainerProperty(CAPTION_PROPERTY, String.class, "");
		targetTree
				.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
		targetTree.setItemCaptionPropertyId(CAPTION_PROPERTY);
		targetTree.setMultiSelect(true);
		targetTree.setStyleName("my-style");
		Panel targetTreeWrapper = new Panel();
		targetTreeWrapper.setWidth("400px");
		targetTreeWrapper.setHeight("100%");
		targetTreeWrapper.setImmediate(false);
		targetTreeWrapper.getContent().setSizeUndefined();
		targetTreeWrapper.addComponent(targetTree);
		hl_inputTab.addComponent(targetTreeWrapper);

		// btnPanel2
		btnPanel2 = buildBtnPanel2();
		hl_inputTab.addComponent(btnPanel2);
		hl_inputTab.setComponentAlignment(btnPanel2, Alignment.MIDDLE_CENTER);

		return hl_inputTab;
	}

	@AutoGenerated
	private VerticalLayout buildBtnPanel1() {
		// common part: create layout
		btnPanel1 = new VerticalLayout();
		btnPanel1.setImmediate(false);
		btnPanel1.setWidth("150px");
		btnPanel1.setHeight("-1px");
		btnPanel1.setMargin(false);
		btnPanel1.setSpacing(true);

		// add
		add = new NativeButton();
		add.setCaption("ADD");
		add.setImmediate(true);
		add.setWidth("90px");
		add.setHeight("30px");
		add.addListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 941928594106974769L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				moveSelectItem(sourceTree, targetTree);
			}

		});
		btnPanel1.addComponent(add);
		btnPanel1.setComponentAlignment(add, Alignment.MIDDLE_CENTER);
		// add_all
		add_all = new NativeButton();
		add_all.setCaption("ADD ALL");
		add_all.setImmediate(true);
		add_all.setWidth("90px");
		add_all.setHeight("30px");
		add_all.addListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8909864437538546744L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				sourceTree.setValue(sourceTree.getItemIds());
				moveSelectItem(sourceTree, targetTree);
			}

		});
		btnPanel1.addComponent(add_all);
		btnPanel1.setComponentAlignment(add_all, Alignment.MIDDLE_CENTER);
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
				moveSelectItem(targetTree, sourceTree);
			}

		});
		btnPanel1.addComponent(remove);
		btnPanel1.setComponentAlignment(remove, Alignment.MIDDLE_CENTER);
		// remove_all
		remove_all = new NativeButton();
		remove_all.setCaption("REMOVE ALL");
		remove_all.setImmediate(true);
		remove_all.setWidth("90px");
		remove_all.setHeight("30px");
		remove_all.addListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8909864437538546744L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				targetTree.setValue(targetTree.getItemIds());
				moveSelectItem(targetTree, sourceTree);
			}

		});
		btnPanel1.addComponent(remove_all);
		btnPanel1.setComponentAlignment(remove_all, Alignment.MIDDLE_CENTER);
		// create
		create = new NativeButton();
		create.setCaption("CREATE");
		create.setImmediate(true);
		create.setWidth("90px");
		create.setHeight("30px");
		create.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				final HandlerForm hf = new HandlerForm();
				hf.center();
				hf.addListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						// TODO Auto-generated method stub
						if (null != hf.getHandlerInfo()) {
							addHandlerItem(hf.getHandlerInfo(), targetTree);
							// handlers.add(hf.getHandlerInfo());
						}
					}
				});
				getWindow().addWindow(hf);
			}
		});
		btnPanel1.addComponent(create);
		btnPanel1.setComponentAlignment(create, Alignment.MIDDLE_CENTER);
		// edit
		edit = new NativeButton();
		edit.setCaption("EDIT");
		edit.setImmediate(true);
		edit.setWidth("90px");
		edit.setHeight("30px");
		edit.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				// System.out.println(sourceTree.getValue().getClass().getName());
				if (((Set) sourceTree.getValue()).size()
						+ ((Set) targetTree.getValue()).size() == 1) {
					if (((Set) sourceTree.getValue()).size() == 1) {
						constructEditForm(sourceTree);
					} else {
						constructEditForm(targetTree);
					}
				} else {
					getWindow().showNotification(
							"Please select 1 handler from Left List");
				}
				getApplication().setTheme("mytheme");
			}
		});
		btnPanel1.addComponent(edit);
		btnPanel1.setComponentAlignment(edit, Alignment.MIDDLE_CENTER);

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
				sourceTree.removeAllItems();
				targetTree.removeAllItems();
				if (contField.getValue().toString().equals("")
						&& containersList.getValue() != null)
					currentContainer = containersList.getValue().toString();
				else
					currentContainer = contField.getValue().toString();
				if (!currentContainer.equals("")) {
					handlers = LoadHandlers();
					fillTree();
				}
			}
		});
		btnPanel1.addComponent(reload);
		btnPanel1.setComponentAlignment(reload, Alignment.MIDDLE_CENTER);
		return btnPanel1;
	}

	@AutoGenerated
	private VerticalLayout buildBtnPanel2() {
		// common part: create layout
		btnPanel2 = new VerticalLayout();
		btnPanel2.setImmediate(false);
		btnPanel2.setWidth("200px");
		btnPanel2.setHeight("-1px");
		btnPanel2.setMargin(false);
		// btnPanel2.setSpacing(true);

		// btn_genAbs
		btn_genAbs = new NativeButton();
		btn_genAbs.setSizeUndefined();
		btn_genAbs.setIcon(new ThemeResource("icon/gen.png"));
		btn_genAbs.setImmediate(false);
		btn_genAbs.addListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8324815917369026554L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				imageByteArray = TriggerMatcher
						.TriggerMatch(selectedHandlers());
				Embedded newPlot = new Embedded(null, getImageResource());
				newPlot.setHeight("-1px");
				newPlot.setWidth("-1px");
				mainTab.addTab(newPlot, getTime()).setClosable(true);
				mainTab.setSelectedTab(newPlot);
			}

		});
		btnPanel2.addComponent(btn_genAbs);
		btnPanel2.setComponentAlignment(btn_genAbs, Alignment.MIDDLE_CENTER);
		// // btn_genComp
		// btn_genComp = new Button();
		// //btn_genComp.setCaption("Abs");
		// btn_genComp.setImmediate(false);
		// btn_genComp.setWidth("100px");
		// btn_genComp.setHeight("100px");
		// btnPanel2.addComponent(btn_genComp);

		return btnPanel2;
	}

	private String getTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(cal.getTime());
	}

	private StreamResource getImageResource() {
		StreamSource imagesource = new MyImageSource();
		StreamResource imageresource = new StreamResource(imagesource, "",
				this.getApplication());
		imageresource.setMIMEType("image/png");
		imageresource.setCacheTime(0);
		return imageresource;
	}

	private void fillTree() {
		for (HandlerInfo hi : handlers) {
			addHandlerItem(hi, sourceTree);
		}
	}

	private void moveSelectItem(Tree source, Tree target) {
		@SuppressWarnings("unchecked")
		Set<Object> selectedIds = (Set<Object>) source.getValue();
		for (Object o : selectedIds) {
			// System.out.println(source.getItem(o));
			if (o.getClass().equals(HandlerInfo.class)) {
				HandlerInfo hi = (HandlerInfo) o;
				if (source.getParent(hi) == null) {
					addHandlerItem(hi, target);
					removeHandlerItem(hi, source);
				}
			}
		}
	}

	private void removeHandlerItem(HandlerInfo hi, Tree tree) {
		Object[] children = tree.getChildren(hi).toArray();
		tree.removeItem(hi);
		tree.removeItem(children[0]);
		tree.removeItem(children[1]);
	}

	private void addHandlerItem(HandlerInfo handler, Tree tree) {
		tree.addItem(handler);
		// get the created item
		Item item = tree.getItem(handler);
		// set our "caption" property
		final Property p = item.getItemProperty(CAPTION_PROPERTY);
		p.setValue(handler.getSlName() + "." + handler.getName());
		Object inputID = tree.addItem();
		tree.getItem(inputID).getItemProperty(CAPTION_PROPERTY)
				.setValue(handler.getInputExpr());
		Object outputID = tree.addItem();
		tree.getItem(outputID).getItemProperty(CAPTION_PROPERTY)
				.setValue(handler.getOutputExpr());
		tree.setParent(inputID, handler);
		tree.setChildrenAllowed(inputID, false);
		tree.setParent(outputID, handler);
		tree.setChildrenAllowed(outputID, false);
	}

	public Set<HandlerInfo> getHandlers() {
		return handlers;
	}

	public void setHandlers(Set<HandlerInfo> handlers) {
		this.handlers = handlers;
	}

	public Set<HandlerInfo> selectedHandlers() {
		Set<HandlerInfo> sHandlers = new HashSet<HandlerInfo>();
		for (Object hi : targetTree.getItemIds()) {
			if (targetTree.getParent(hi) == null)
				sHandlers.add((HandlerInfo) hi);
		}
		return sHandlers;
	}

	class MyImageSource implements StreamResource.StreamSource {
		@Override
		public InputStream getStream() {
			return new ByteArrayInputStream(imageByteArray);
		}
	}

	/*
	 * We need to implement this method that returns the resource as a stream.
	 */

	private Set<HandlerInfo> LoadHandlers() {
		Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
		try {
			// String[] objList = oClient.getContainerContents("TestTenant",
			// "edt-testChain6");
			logger.info("storlet loading");
			String[] slList = oClient.getObjectIdsForMetadata(tenant,
					currentContainer, SPEConstants.STORLET_TAG_TRIGGERS);
			if (slList == null)
				return handlers;

			for (String slName : slList) {
				logger.info(slName + " storlet loaded");
				String outputs = oClient.getObjectMetadataEntry(tenant,
						currentContainer, slName, "_slOutputs");
				Trigger[] triggers = Trigger.createTriggers(oClient
						.getObjectMetadataEntry(tenant, currentContainer,
								slName, SPEConstants.STORLET_TAG_TRIGGERS));

				if (triggers != null) {
					for (Trigger trigger : triggers) {
						if (outputs != null) {
							JSONObject obj = JSONObject.fromObject(outputs);
							if (obj.containsKey(trigger.getHandlerID())) {
								handlers.add(new HandlerInfo(slName, trigger
										.getHandlerID(), trigger
										.getTriggerEvaluator(), obj
										.getString(trigger.getHandlerID()),
										false));
							} else {
								handlers.add(new HandlerInfo(slName, trigger
										.getHandlerID(), trigger
										.getTriggerEvaluator(), "oNA", false));
							}
						} else {
							handlers.add(new HandlerInfo(slName, trigger
									.getHandlerID(), trigger
									.getTriggerEvaluator(), "oNA", false));
						}
					}
				}

				// }
			}
		} catch (ContentCentricException e) {
			// TODO Auto-generated catch block
			logger.error("load handlers failed ", e);
		}
		return handlers;
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

	private void constructEditForm(final Tree tree) {
		if (((Set) tree.getValue()).size() == 1
				&& ((Set) tree.getValue()).iterator().next().getClass()
						.equals(HandlerInfo.class)) {
			final HandlerInfo selectedhandler = (HandlerInfo) ((Set) tree
					.getValue()).iterator().next();
			final HandlerForm hf = new HandlerForm(selectedhandler);
			hf.center();
			hf.addListener(new Window.CloseListener() {
				@Override
				public void windowClose(CloseEvent e) {
					// TODO Auto-generated method stub
					if (null != hf.getHandlerInfo()) {
						
						removeHandlerItem(selectedhandler, tree);
						// handlers.remove(selectedhandler);
						addHandlerItem(hf.getHandlerInfo(), tree);
						// handlers.add(hf.getHandlerInfo());
					}
				}
			});
			getWindow().addWindow(hf);
		}
	}
}
