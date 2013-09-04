package eu.visioncloud.workflowengine.obj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EvalConstruct {
	private String newiExpr = "";
	private String newoExpr = "";
	private List<Attribute> inputAtoms = new ArrayList<Attribute>();
	private List<Attribute> outputAtoms = new ArrayList<Attribute>();
	Map atoms = new HashMap();

	public EvalConstruct(String inputExpr, String outputExpr) {
		if (inputExpr != null) {
			inputAtoms = AtomExtractor(inputExpr, "ikv", atoms);
			Collections.sort(inputAtoms, new AttrComparator());
			newiExpr = ExpressionFormatter(inputAtoms, inputExpr);
		}
		if (outputExpr != null) {
			outputAtoms = AtomExtractor(outputExpr, "okv", atoms);
			Collections.sort(outputAtoms, new AttrComparator());
			newoExpr = ExpressionFormatter(outputAtoms, outputExpr);
		}
	}

	public String getNewiExpr() {
		return newiExpr;
	}

	public String getNewoExpr() {
		return newoExpr;
	}

	public List<Attribute> getInputAtoms() {
		return inputAtoms;
	}

	public List<Attribute> getOutputAtoms() {
		return outputAtoms;
	}

	public List<Attribute> AtomExtractor(String expr, String flag, Map atoms) {
		List<Attribute> l = new ArrayList();
		expr = expr.replaceAll("'\\('|'\\)'", "");// distinguish () in logical
													// expr and regex as '(' +
													// ')'
		String exprList[] = expr.split("&&|\\|\\|");
		for (String kvpExpr : exprList) {
			// System.out.println("kvpExpr "+kvpExpr);
			String id = flag + l.size();
			String kvp[] = kvpExpr.split("\"");
			// System.out.println(kvp.length);
			if (kvp.length == 4) {
				l.add(new Attribute(id, kvp[1], kvp[2], kvp[3]));
			} else if (kvp.length == 3) {
				// System.out.println("enter");
				l.add(new Attribute(id, kvp[1], "ALL", ""));
			}
			atoms.put(id, kvpExpr);
			// System.out.println("Corrrrrect? " + kvpExpr);
		}

		// System.out.println(l.get(0).id+l.get(1).id+l.get(2).id);
		return l;
	}

	public String ExpressionFormatter(List<Attribute> kvs, String expr) {
		for (Attribute attr : kvs) {
			String kv;
			if (attr.getOperator().equals("ALL")) {
				// System.out.println("Corrrrrect? " + expr);
				kv = "\"" + attr.getKey() + "\"_exists";
			} else {
				// if (attr.getClass().equals(SpecAttribute.class)){
				// kv="\""+attr.getKey()+"\""+attr.getOperator()+"\""+attr.getValue()+"\"";
				// }
				// else
				kv = "\"" + attr.getKey() + "\"" + attr.getOperator() + "\""
						+ attr.getValue() + "\"";
				// System.out.println("kv " + kv);
			}

			// System.out.println("kv "+ Pattern.quote(kv));
			String atom = attr.getId();
			if (kv.contains("Quality")) {
				// System.out.println("error kv "+ kv);
				// System.out.println("expr kv "+ Pattern.quote(kv));
				// System.out.println("eerror atom "+ atom);
			}
			expr = expr.replaceAll(Pattern.quote(kv), atom); // literally
																// escapping
			// expr = expr.replaceAll(kv, atom);
		}
		// expr = expr.replaceAll("'\\('", "\\(").replaceAll("'\\)'", "\\)");
		// System.out.println("expr "+ expr);
		return expr;
	}

}
