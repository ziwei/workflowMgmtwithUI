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

public class HandlerInfo{

private String name;
private String inputExpr;
private String outputExpr;
//appear
private String a_inputExpr;
private String a_outputExpr;
private String a_newiExpr;
private String a_newoExpr;
private List<Attribute> a_inputAtoms;
private List<Attribute> a_outputAtoms;
private Map a_atoms;
//disappear

//constant

private boolean acyclic;

public HandlerInfo(){

}
public HandlerInfo(String n, String i, String o){
	setName(n);
	setInputExpr(i);
	setOutputExpr(o);
	//appear
	setAppearAtoms(new HashMap());
	setAppearInputAtoms(AtomExtractor(getAppearInputExpr(), "ikv", a_atoms));
	setAppearOutputAtoms(AtomExtractor(getAppearOutputExpr(), "okv", a_atoms));
	Collections.sort(getAppearInputAtoms(), new AttrComparator());
	Collections.sort(getAppearOutputAtoms(), new AttrComparator());
	setAppearNewiExpr(ExpressionFormatter(getAppearInputAtoms(), getAppearInputExpr()));
	setAppearNewoExpr(ExpressionFormatter(getAppearOutputAtoms(), getAppearOutputExpr()));
	setAcyclic(true);
}
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
	this.name = name;
}
public String getInputExpr() {
	return inputExpr;
}
public void setInputExpr(String inputExpr) {
	this.inputExpr = inputExpr;
	setAppearInputExpr(inputExpr.split(",")[0].substring(8, inputExpr.split(",")[0].length()-1));
	//System.out.println(getAppearInputExpr());
}
public String getOutputExpr() {
	return outputExpr;
}
public void setOutputExpr(String outputExpr) {
	this.outputExpr = outputExpr;
	setAppearOutputExpr(outputExpr.split(",")[0].substring(8, outputExpr.split(",")[0].length()-1));
}
public String getAppearInputExpr() {
	return a_inputExpr;
}
public void setAppearInputExpr(String inputExpr) {
	this.a_inputExpr = inputExpr;
}
public String getAppearOutputExpr() {
	return a_outputExpr;
}
public void setAppearOutputExpr(String outputExpr) {
	this.a_outputExpr = outputExpr;
}
public Map getAppearAtoms() {
	return a_atoms;
}
public void setAppearAtoms(Map atoms) {
	this.a_atoms = atoms;
}
public String getAppearNewoExpr() {
	return a_newoExpr;
}
public void setAppearNewoExpr(String newoExpr) {
	this.a_newoExpr = newoExpr;
}
public List<Attribute> getAppearOutputAtoms() {
	return a_outputAtoms;
}
public void setAppearOutputAtoms(List<Attribute> outputAtoms) {
	this.a_outputAtoms = outputAtoms;
}
public List<Attribute> getAppearInputAtoms() {
	return a_inputAtoms;
}
public void setAppearInputAtoms(List<Attribute> inputAtoms) {
	this.a_inputAtoms = inputAtoms;
}
public String getAppearNewiExpr() {
	return a_newiExpr;
}
public void setAppearNewiExpr(String newiExpr) {
	this.a_newiExpr = newiExpr;
}
public List<Attribute> AtomExtractor (String expr, String flag, Map atoms){
	List<Attribute> l = new ArrayList();
	expr = expr.replaceAll("'\\('|'\\)'", "");//distinguish () in logical expr and regex as '(' + ')'
	String exprList[] = expr.split("&&|\\|\\|");
	for(String kvpExpr : exprList){
		//System.out.println("kvpExpr "+kvpExpr);
		String id = flag+l.size();
		String kvp[] = kvpExpr.split("\"");
		if (kvp.length == 4){
			if (kvp[3].startsWith("'"))
				l.add(new Attribute(id, kvp[1],kvp[2],kvp[3].substring(1, kvp[3].length()-1)));
			else
				l.add(new SpecAttribute(id,kvp[1],kvp[2],kvp[3]));
		}
		else if (kvp.length == 2){
			//System.out.println("enter");
			l.add(new Attribute(id,kvp[1],"ALL",""));
		}
		atoms.put(id, kvpExpr);
		//System.out.println("Corrrrrect? " + kvpExpr);
	}
	
	//System.out.println(l.get(0).id+l.get(1).id+l.get(2).id);
	return l;
}
public String ExpressionFormatter(List<Attribute> kvs, String expr){
	for (Attribute attr : kvs){
		String kv;
		if (attr.getOperator().equals("ALL")){
			//System.out.println("Corrrrrect? " + expr);
			kv= "\""+attr.getKey()+"\"";
		}
		else{
			if (attr.getClass().equals(SpecAttribute.class)){
				kv="\""+attr.getKey()+"\""+attr.getOperator()+"\""+attr.getValue()+"\"";
			}
			else
				kv="\""+attr.getKey()+"\""+attr.getOperator()+"\"'"+attr.getValue()+"'\"";
			//System.out.println("kv " + kv);
		}
			
		//System.out.println("kv "+ Pattern.quote(kv));
		String atom = attr.getId();
		if (kv.contains("Quality")){
			//System.out.println("error kv "+ kv);
			//System.out.println("expr kv "+ Pattern.quote(kv));
			//System.out.println("eerror atom "+ atom);
			}
		expr = expr.replaceAll(Pattern.quote(kv), atom); //literally escapping
		//expr = expr.replaceAll(kv, atom);
	}
	expr = expr.replaceAll("'\\('", "\\(").replaceAll("'\\)'", "\\)");
	return expr;
}
}
