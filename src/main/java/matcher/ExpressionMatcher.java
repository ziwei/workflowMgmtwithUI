package matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.*;

import obj.Attribute;
import obj.SpecAttribute;

import orbital.logic.functor.Predicates;
import orbital.logic.imp.Formula;
import orbital.logic.imp.Logic;
import orbital.logic.sign.ParseException;
import orbital.moon.logic.ClassicalLogic;
import orbital.moon.logic.ClassicalLogic.InferenceMechanism;

public class ExpressionMatcher {
	public Logic logic = new ClassicalLogic();
	
	
	public ExpressionMatcher(){
		//logic = new ClassicalLogic();
	}
	
	public Map Prove(Formula[] axioms, String l, String r) throws IllegalArgumentException, ParseException{
		Map res = new HashMap();
		System.out.println(l+" "+r);
		String[] CFs = DNFSplit((Formula)logic.createExpression(l));
		Formula right = (Formula)logic.createExpression(r);
		//Formula fal = (Formula)logic.createExpression("false");
		for(String CF : CFs){
			Formula lCNF = (Formula)logic.createExpression(CF);
			System.out.println("to prove 1 : "+lCNF.impl(right));
			//System.out.println(axioms[0]+" "+axioms[1]+" "+axioms[2]+" "+axioms[3]);
			if(logic.inference().infer(axioms, lCNF.impl(right))){
				res.put(lCNF.toString(), 0);
			}
			else{
				String[] DFs = CNFSplit(right);
				//System.out.println(DFs[0]+" "+DFs[1]);
				boolean partial = false;
				for(String DF : DFs){
					//System.out.println(DFs[j]);
					Formula rDNF = (Formula)logic.createExpression(DF);
					System.out.println("to prove 2 : " + lCNF.impl(rDNF));
					if(axioms.length > 1)
						System.out.println(axioms[1]);
					if(logic.inference().infer(axioms, (lCNF.impl(rDNF)))){
						res.put(lCNF.toString(), 1);
						partial = true;
						break;
					}
				}
				if(partial == false)
					res.put(lCNF.toString(), 2);
			}
		}	
		return res;
	}
	public Formula[] AxiomsGen(List<Attribute> output, List<Attribute> input) throws IllegalArgumentException, ParseException{
		ArrayList<Formula> statements = new ArrayList<Formula>();
		for(Attribute iAttr : input){
			if (iAttr.getClass().equals(SpecAttribute.class)){
				Attribute leftHigh = null;
				Attribute leftLow = null;
				Attribute rightHigh = null;
				Attribute rightLow = null;
				boolean isNotification = false;
				for(Attribute oAttr : output){
					if (!oAttr.getKey().equals("N/A")){
						if (oAttr.getKey().equals(iAttr.getKey())){
							if (leftHigh == null || oAttr.getValue().compareTo(leftHigh.getValue()) > 0)
								leftHigh = oAttr;
							if (leftLow == null || oAttr.getValue().compareTo(leftLow.getValue()) < 0)
								leftLow = oAttr;
						}
						if (oAttr.getKey().equals(iAttr.getValue())){
							if (rightHigh == null || oAttr.getValue().compareTo(rightHigh.getValue()) > 0)
								rightHigh = oAttr;
							if (rightLow == null || oAttr.getValue().compareTo(rightLow.getValue()) < 0)
								rightLow = oAttr;
						}
					}
					else {
						isNotification = true;break;
					}
				}
				if (isNotification == false){
					String res = ValueMatcher.SpecValueMatch(leftHigh, leftLow, rightHigh, rightLow, (SpecAttribute)iAttr);
					if (null != res){
						Formula axiom  = (Formula)logic.createExpression(res);
						statements.add(axiom);
					}
				}
				
			}
			else if(iAttr.getClass().equals(Attribute.class)){
				for(Attribute oAttr : output){
					if (oAttr.getKey().equals(iAttr.getKey())){
						String sameKeyState = ValueMatcher.ValueMatch(oAttr, iAttr);
						if (null != sameKeyState){
							Formula fState  = (Formula)logic.createExpression(sameKeyState);
							statements.add(fState);
						}
					}
					
				}
			}
		}
		//System.out.println(statements);
		return statements.toArray(new Formula[statements.size()]);
	}
	
	private String[] DNFSplit(Formula f){
		Formula formula = ClassicalLogic.Utilities.disjunctiveForm(f, true);
		String DNF = formula.toString();
		String[] CFs = DNF.split("\\s\\|\\s");
		return CFs;
	}
	public String DNFTransfer(String formula) throws IllegalArgumentException, ParseException{
		Formula dnf = ClassicalLogic.Utilities.disjunctiveForm((Formula)logic.createExpression(formula), true);
		return dnf.toString();
	}
	
	private String[] CNFSplit(Formula f){
		Formula fomula = ClassicalLogic.Utilities.conjunctiveForm(f, true);
		String CNF = fomula.toString();
		String[] DFs = CNF.split("\\s\\&\\s");
		return DFs;
	}
	
	
	
}
