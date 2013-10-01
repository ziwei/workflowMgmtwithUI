/*
 * class for comparing two values if they share the same key,
 * the values are together with their operator
 */
package eu.visioncloud.workflowengine.matcher;

import java.util.List;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import eu.visioncloud.workflowengine.obj.Attribute;
import eu.visioncloud.workflowengine.obj.SpecAttribute;

public class ValueMatcher {
	public static final int LIMIT = 25;

	public static String ValueMatch(Attribute output, Attribute input) {
		if (null == output || null == input)
			return null;
		String implies = output.getId() + "->(" + input.getId() + ")";
		// String implies =
		// "("+input.getId()+"->~"+output.getId()+")|("+input.getId()+"->"+output.getId()+")";

		String notImplies = input.getId() + "->~(" + output.getId() + ")";

		String strongImp = output.getId() + "->(" + input.getId() + ")";
		// String notImplies = null;
		if (output.getOperator().equals("=")) {
			if (input.getOperator().equals("=")) {
				return output.getValue().equals(input.getValue()) ? strongImp
						: notImplies;
			} else if (input.getOperator().equals(">")) {
				// System.out.println("error might be "+
				// output.getValue()+" "+(input.getValue())+" "+output.getValue().compareTo(input.getValue()));
				return output.getValue().compareTo(input.getValue()) > 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">=")) {
				return output.getValue().compareTo(input.getValue()) > -1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<")) {
				return output.getValue().compareTo(input.getValue()) < 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<=")) {
				return output.getValue().compareTo(input.getValue()) < 1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("~")) {
				Automaton intersec = BasicOperations.intersection(
						ToAutomaton(output), ToAutomaton(input));
				return (intersec.getNumberOfTransitions() > 0) ? implies
						: notImplies;
			} else if (input.getOperator().equals("ALL")) {
				return implies;
			}
		} else if (output.getOperator().equals("~")) {
			// System.out.println("OK till here2");
			Automaton o = ToAutomaton(output);
			String maxStr = MaxString(o, LIMIT);
			String minStr = MinString(o, LIMIT);
			// System.out.println("OK till here3");
			if (input.getOperator().equals("=")) {
				Automaton intersec = BasicOperations.intersection(o,
						ToAutomaton(input));
				return (intersec.getNumberOfTransitions() > 0) ? implies
						: notImplies;
			} else if (input.getOperator().equals(">")) {
				// System.out.println("OK till here "+maxStr.compareTo(input.value));
				return maxStr.compareTo(input.getValue()) > 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">=")) {
				return maxStr.compareTo(input.getValue()) > -1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<")) {
				return minStr.compareTo(input.getValue()) < 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<=")) {
				return minStr.compareTo(input.getValue()) < 1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("~")) {
				Automaton intersec = BasicOperations.intersection(
						ToAutomaton(output), ToAutomaton(input));
				return (intersec.getNumberOfTransitions() > 0) ? implies
						: notImplies;
			} else if (input.getOperator().equals("ALL")) {
				return implies;
			}
		} else if (output.getOperator().equals("<")) {
			if (input.getOperator().equals("=")) {
				return output.getValue().compareTo(input.getValue()) > 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">")) {
				return output.getValue().compareTo(input.getValue()) > 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">=")) {
				return output.getValue().compareTo(input.getValue()) > 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<")) {
				return implies;
			} else if (input.getOperator().equals("<=")) {
				return implies;
			} else if (input.getOperator().equals("~")) {
				Automaton i = ToAutomaton(input);
				String minStr = MinString(i, LIMIT);
				return output.getValue().compareTo(minStr) > 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("ALL")) {
				return implies;
			}
		} else if (output.getOperator().equals("<=")) {
			if (input.getOperator().equals("=")) {
				return output.getValue().compareTo(input.getValue()) > -1 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">")) {
				return output.getValue().compareTo(input.getValue()) > 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">=")) {
				return output.getValue().compareTo(input.getValue()) > -1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<")) {
				return implies;
			} else if (input.getOperator().equals("<=")) {
				return implies;
			} else if (input.getOperator().equals("~")) {
				Automaton i = ToAutomaton(input);
				String minStr = MinString(i, LIMIT);
				return output.getValue().compareTo(minStr) > -1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("ALL")) {
				return implies;
			}
		} else if (output.getOperator().equals(">")) {
			if (input.getOperator().equals("=")) {
				return output.getValue().compareTo(input.getValue()) < 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">")) {
				return implies;
			} else if (input.getOperator().equals(">=")) {
				return implies;
			} else if (input.getOperator().equals("<")) {
				return output.getValue().compareTo(input.getValue()) < 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<=")) {
				return output.getValue().compareTo(input.getValue()) < 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("~")) {
				Automaton i = ToAutomaton(input);
				String maxStr = MaxString(i, LIMIT);
				return output.getValue().compareTo(maxStr) < 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("ALL")) {
				return implies;
			}
		} else if (output.getOperator().equals(">=")) {
			if (input.getOperator().equals("=")) {
				return output.getValue().compareTo(input.getValue()) > -1 ? implies
						: notImplies;
			} else if (input.getOperator().equals(">")) {
				return implies;
			} else if (input.getOperator().equals(">=")) {
				return implies;
			} else if (input.getOperator().equals("<")) {
				return output.getValue().compareTo(input.getValue()) < 0 ? implies
						: notImplies;
			} else if (input.getOperator().equals("<=")) {
				return output.getValue().compareTo(input.getValue()) < 1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("~")) {
				Automaton i = ToAutomaton(input);
				String maxStr = MaxString(i, LIMIT);
				return output.getValue().compareTo(maxStr) < 1 ? implies
						: notImplies;
			} else if (input.getOperator().equals("ALL")) {
				return implies;
			}
		} else if (output.getOperator().equals("ALL"))
			return implies;

		return null;
	}

	public static String SpecValueMatch(Attribute leftHigh, Attribute leftLow,
			Attribute rightHigh, Attribute rightLow, SpecAttribute sa) {
		// String implies = left.getId()+"->("+right.getId()+")";
		// String notImplies = right.getId()+"->~("+left.getId()+")";
		String lUpper = null;
		if (null != leftHigh)
			lUpper = UpperBoundary(leftHigh.getOperator(), leftHigh.getValue());
		String lLower = null;
		if (null != leftLow)
			lLower = LowerBoundary(leftLow.getOperator(), leftLow.getValue());
		String rUpper = null;
		if (null != rightHigh)
			rUpper = UpperBoundary(rightHigh.getOperator(),
					rightHigh.getValue());
		String rLower = null;
		if (null != rightLow)
			rLower = LowerBoundary(rightLow.getOperator(), rightLow.getValue());
		// System.out.println("lUpper " + lUpper);
		// System.out.println("rLower " + rLower);
		if (sa.getOperator().equals("=")) {
			if ((null != leftHigh && null != rightLow)
					&& ValueMatcher.ValueMatch(leftHigh, rightLow).equals(
							leftHigh.getId() + "->(" + rightLow.getId() + ")"))
				return sa.getId();
			else if ((null != leftLow && null != rightHigh)
					&& ValueMatcher.ValueMatch(leftLow, rightHigh).equals(
							leftLow.getId() + "->(" + rightHigh.getId() + ")"))
				return sa.getId();
			else
				return null;
		} else if (sa.getOperator().equals(">")) {
			if (null != lUpper) {
				if (null != rLower) {
					if (lUpper.compareTo(rLower) < 1)
						return null;
				}
			}
			return sa.getId();
		} else if (sa.getOperator().equals(">=")) {
			if (null != lUpper) {
				if (null != rLower) {
					if (lUpper.compareTo(rLower) < 1)
						return null;
				}
			}
			if (ValueMatcher.ValueMatch(leftHigh, rightLow).equals(
					rightLow.getId() + "->~(" + leftHigh.getId() + ")")
					&& ValueMatcher.ValueMatch(leftLow, rightHigh).equals(
							rightHigh.getId() + "->~(" + leftLow.getId() + ")"))
				return null;

			return sa.getId();
		} else if (sa.getOperator().equals("<")) {
			if (null != lLower) {
				if (null != rUpper) {
					if (lLower.compareTo(rUpper) > -1)
						return null;
				}
			}
			return sa.getId();
		} else if (sa.getOperator().equals("<=")) {
			if (null != lLower) {
				if (null != rUpper) {
					if (lLower.compareTo(rUpper) > -1)
						return null;
				}
			}
			if (ValueMatcher.ValueMatch(leftHigh, rightLow).equals(
					rightLow.getId() + "->~(" + leftHigh.getId() + ")")
					&& ValueMatcher.ValueMatch(leftLow, rightHigh).equals(
							rightHigh.getId() + "->~(" + leftLow.getId() + ")"))
				return null;

			return sa.getId();
		}
		return null;
	}

	private static String UpperBoundary(String oper, String value) {
		if (oper.equals(">") || oper.equals(">=")) {
			return null;
		} else if (oper.equals("=") || oper.equals("<") || oper.endsWith("<=")) {
			return value;
		}
		return null;
	}

	private static String LowerBoundary(String oper, String value) {
		if (oper.equals("<") || oper.equals("<=")) {
			return null;
		} else if (oper.equals("=") || oper.equals(">") || oper.endsWith(">=")) {
			return value;
		}
		return null;
	}

	private static Automaton ToAutomaton(Attribute attr) {
		RegExp regex = new RegExp(attr.getValue());
		return regex.toAutomaton();
	}

	private static String MaxString(Automaton a, int limit) {// Derive max
																// string of a
																// regex in
																// Automaton
																// format
		String maxStr = "";
		State state = a.getInitialState();
		while (!state.isAccept()) {
			List<Transition> lTrans = state.getSortedTransitions(true);
			char maxChar = '\u0000';
			for (Transition t : lTrans) {
				if (maxChar <= t.getMax()) {
					maxChar = t.getMax();
					state = t.getDest();
					// System.out.println(state);
				}
			}
			maxStr += maxChar;
			if (maxStr.length() >= limit)
				break;
		}
		return maxStr;
	}

	private static String MinString(Automaton a, int limit) {// Derive max
																// string of a
																// regex in
																// Automaton
																// format
		String minStr = "";
		State state = a.getInitialState();
		while (!state.isAccept()) {
			List<Transition> lTrans = state.getSortedTransitions(true);
			char minChar = '\uffff';
			for (Transition t : lTrans) {
				if (minChar <= t.getMin()) {
					minChar = t.getMin();
					state = t.getDest();
					// System.out.println(state);
				}
			}
			minStr += minChar;
			if (minStr.length() >= limit)
				break;
		}
		return minStr;
	}

}
