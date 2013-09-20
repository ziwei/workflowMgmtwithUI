package eu.visioncloud.workflowengine.obj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import com.google.gwt.user.client.rpc.IsSerializable;;
//import java.util.regex.Pattern;
import java.util.regex.Pattern;

public class HandlerInfo {
	private String slName;
	private String name;
	private String inputExpr;
	private String outputExpr;
	// appear
	String a_inputExpr;
	String a_outputExpr;
	private EvalConstruct appear;

	// disappear
	String d_inputExpr;
	String d_outputExpr;
	private EvalConstruct disappear;
	// constant
	String c_inputExpr;
	String c_outputExpr;
	private EvalConstruct constant;

	private boolean acyclic;
	private boolean isDirty = false;

	public HandlerInfo() {

	}

	public HandlerInfo(String slName, String n, String i, String o, boolean d) {
		setSlName(slName);
		setName(n);
		setInputExpr(i);
		setOutputExpr(o);
		// appear

		setAppear(new EvalConstruct(a_inputExpr, a_outputExpr));

		// disappear

		setDisappear(new EvalConstruct(d_inputExpr, d_outputExpr));

		// constant

		setConstant(new EvalConstruct(c_inputExpr, c_outputExpr));

		setAcyclic(true);
		setDirty(d);
	}

	// public HandlerInfo(String n, String i, String o) {
	// setName(n);
	// setInputExpr(i);
	// setOutputExpr(o);
	// // appear
	//
	// setAppear(new EvalConstruct(a_inputExpr, a_outputExpr));
	//
	// // disappear
	//
	// setDisappear(new EvalConstruct(d_inputExpr, d_outputExpr));
	//
	// // constant
	//
	// setConstant(new EvalConstruct(c_inputExpr, c_outputExpr));
	//
	// setAcyclic(true);
	// }

	public boolean isAcyclic() {
		return acyclic;
	}

	public void setAcyclic(boolean acyclic) {
		this.acyclic = acyclic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (!name.equals("")&&name!=null)
			this.name = name;
		else
			this.name = "DEFAULT";
	}

	public String getInputExpr() {
		return inputExpr;
	}

	public void setInputExpr(String inputExpr) {
		this.inputExpr = inputExpr.replaceAll("\\s", "");
		String[] evals = this.inputExpr.split(",");
		for (String eval : evals) {
			if (eval.startsWith("appear")&&eval.length()>8)
				a_inputExpr = eval.substring(8, eval.length() - 1);
			else if (eval.startsWith("disappear")&&eval.length()>11)
				d_inputExpr = eval.substring(11, eval.length() - 1);
			else if (eval.startsWith("constant")&&eval.length()>10)
				c_inputExpr = eval.substring(10, eval.length() - 1);
		}// System.out.println(getAppearInputExpr());
	}

	public String getOutputExpr() {
		return outputExpr;
	}

	public void setOutputExpr(String outputExpr) {
		this.outputExpr = outputExpr.replaceAll("\\s", "");
		String[] evals = this.outputExpr.split(",");
		for (String eval : evals) {
			if (eval.startsWith("appear")&&eval.length()>8)
				a_outputExpr = eval.substring(8, eval.length() - 1);
			else if (eval.startsWith("disappear")&&eval.length()>11)
				d_outputExpr = eval.substring(11, eval.length() - 1);
			else if (eval.startsWith("constant")&&eval.length()>10)
				c_outputExpr = eval.substring(10, eval.length() - 1);
		}// System.out.println(getAppearInputExpr());
	}

	public EvalConstruct getAppear() {
		return appear;
	}

	public void setAppear(EvalConstruct appear) {
		this.appear = appear;
	}

	public EvalConstruct getDisappear() {
		return disappear;
	}

	public void setDisappear(EvalConstruct disappear) {
		this.disappear = disappear;
	}

	public EvalConstruct getConstant() {
		return constant;
	}

	public void setConstant(EvalConstruct constant) {
		this.constant = constant;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	public String getSlName() {
		return slName;
	}

	public void setSlName(String slName) {
		if (!slName.equals("")&&slName!=null)
			this.slName = slName;
		else
			this.slName = "DEFAULT";
	}

}
