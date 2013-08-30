package test;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;

public class test extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private VerticalLayout verticalLayout_1;
	@AutoGenerated
	private NativeButton nativeButton_3;
	@AutoGenerated
	private NativeButton nativeButton_2;
	@AutoGenerated
	private NativeButton nativeButton_1;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public test() {
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
		
		// verticalLayout_1
		verticalLayout_1 = buildVerticalLayout_1();
		mainLayout.addComponent(verticalLayout_1,
				"top:60.0px;bottom:72.0px;left:160.0px;");
		
		return mainLayout;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_1() {
		// common part: create layout
		verticalLayout_1 = new VerticalLayout();
		verticalLayout_1.setImmediate(false);
		verticalLayout_1.setWidth("30px");
		verticalLayout_1.setHeight("-1px");
		verticalLayout_1.setMargin(false);
		
		// nativeButton_1
		nativeButton_1 = new NativeButton();
		nativeButton_1.setCaption("NativeButton");
		nativeButton_1.setImmediate(true);
		nativeButton_1.setWidth("30px");
		nativeButton_1.setHeight("30px");
		verticalLayout_1.addComponent(nativeButton_1);
		verticalLayout_1.setComponentAlignment(nativeButton_1,
				new Alignment(48));
		
		// nativeButton_2
		nativeButton_2 = new NativeButton();
		nativeButton_2.setCaption("NativeButton");
		nativeButton_2.setImmediate(true);
		nativeButton_2.setWidth("30px");
		nativeButton_2.setHeight("30px");
		verticalLayout_1.addComponent(nativeButton_2);
		verticalLayout_1.setComponentAlignment(nativeButton_2,
				new Alignment(48));
		
		// nativeButton_3
		nativeButton_3 = new NativeButton();
		nativeButton_3.setCaption("NativeButton");
		nativeButton_3.setImmediate(true);
		nativeButton_3.setWidth("30px");
		nativeButton_3.setHeight("30px");
		verticalLayout_1.addComponent(nativeButton_3);
		verticalLayout_1.setComponentAlignment(nativeButton_3,
				new Alignment(48));
		
		return verticalLayout_1;
	}

}
