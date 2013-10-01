/*
 * Generate Axioms from the extracted atoms, transform trigger and output expressions into DNF
 *  & CNF, check the implication is true or not
 */
package eu.visioncloud.workflowengine.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.*;

import org.apache.log4j.Logger;

import eu.visioncloud.workflowengine.obj.Attribute;
import eu.visioncloud.workflowengine.obj.SpecAttribute;

import orbital.logic.functor.Predicates;
import orbital.logic.imp.Formula;
import orbital.logic.imp.Logic;
import orbital.logic.sign.ParseException;
import orbital.moon.logic.ClassicalLogic;
import orbital.moon.logic.ClassicalLogic.InferenceMechanism;

public class ExpressionMatcher {
	public Logic logic = new ClassicalLogic();
	private static final Logger logger = Logger.getLogger("JettyServer");
	public ExpressionMatcher() {
		// logic = new ClassicalLogic();
	}
	
	//Determines if the output -> trigger is a complete, partial trigger or unrelated
	public Map Prove(Formula[] axioms, String l, String r) {
		Map res = new HashMap();
		// System.out.println(l+" "+r);
		if (l.equals("oNA") && r.equals("iNA")) {
			return res;
		} else if (l.equals("oNA") && !r.equals("iNA")) {
			res.put(l, 2);
			return res;
		} else if (!l.equals("oNA") && r.equals("iNA")) {
			res.put(l, 2);
			return res;
		} else {
			String[] CFs;
			try {
				CFs = DNFSplit((Formula) logic.createExpression(l));
				Formula right = (Formula) logic.createExpression(r);
				// System.out.println(r +" : "+right.toString());
				for (String CF : CFs) {
					Formula lCNF = (Formula) logic.createExpression(CF);
					// System.out.println("to prove 1 : "+lCNF.impl(right));
					// System.out.println(axioms[0]+" "+axioms[1]+" "+axioms[2]+" "+axioms[3]);
					if (logic.inference().infer(axioms, lCNF.impl(right))) {
						res.put(lCNF.toString(), 0);
					} else {
						String[] DFs = CNFSplit(right);
						// System.out.println(DFs[0]+" "+DFs[1]);
						boolean partial = false;
						for (String DF : DFs) {
							// System.out.println(DFs[j]);
							Formula rDNF = (Formula) logic.createExpression(DF);
							// System.out.println("to prove 2 : " +
							// lCNF.impl(rDNF));
							// if(axioms.length > 1)
							// System.out.println(axioms[1]);
							if (logic.inference().infer(axioms,
									(lCNF.impl(rDNF)))) {
								res.put(lCNF.toString(), 1);
								partial = true;
								break;
							}
						}
						if (partial == false)
							res.put(lCNF.toString(), 2);
					}
				}
				return res;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logger.info("IllegalArgument ",e);
				return null;
			} catch (ParseException e) {
				logger.info("Parse error ",e);
				return null;
			}
		}
		// Formula fal = (Formula)logic.createExpression("false");

	}

	public Formula[] AxiomsGen(List<Attribute> output, List<Attribute> input) {
		ArrayList<Formula> statements = new ArrayList<Formula>();
		for (Attribute iAttr : input) {
		
			for (Attribute oAttr : output) {
				if (oAttr.getKey().equals(iAttr.getKey())) {
					String sameKeyState = ValueMatcher.ValueMatch(oAttr, iAttr);
					// System.out.println("valuematchres "+ sameKeyState);
					if (null != sameKeyState) {
						Formula fState;
						try {
							fState = (Formula) logic
									.createExpression(sameKeyState);
							statements.add(fState);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							logger.info("IllegalArgument ",e);
						} catch (ParseException e) {
							logger.info("Parse Error ",e);
						}

					}
				}

			}
			// }
		}
		// System.out.println(statements);
		return statements.toArray(new Formula[statements.size()]);
	}

	private String[] DNFSplit(Formula f) {
		Formula formula = ClassicalLogic.Utilities.disjunctiveForm(f, true);
		String DNF = formula.toString();
		String[] CFs = DNF.split("\\s\\|\\s");
		return CFs;
	}

	public String DNFTransfer(String formula) {
		Formula dnf;
		try {
			dnf = ClassicalLogic.Utilities.disjunctiveForm(
					(Formula) logic.createExpression(formula), true);
			return dnf.toString();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			logger.info("IllegalArgument ",e);
		} catch (ParseException e) {
			logger.info("Parse Error ",e);
		}
		return null;
	}

	private String[] CNFSplit(Formula f) {
		Formula fomula = ClassicalLogic.Utilities.conjunctiveForm(f, true);
		String CNF = fomula.toString();
		String[] DFs = CNF.split("\\s\\&\\s");
		return DFs;
	}

}
