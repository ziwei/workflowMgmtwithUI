package eu.visioncloud.workflowengine.obj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TransitionInfo {
	private HandlerInfo from;
	private HandlerInfo to;
	// private ArrayList complete = new ArrayList();
	// private ArrayList partial = new ArrayList();
	// private ArrayList unrelated = new ArrayList();
	private char[] triggerTypes = new char[3];
	private boolean acyclic;
	private String finalType;

	public TransitionInfo() {
	}

	public TransitionInfo(HandlerInfo f, HandlerInfo t, Map appearResult,
			Map disappearResult, Map constantResult) {
		setFrom(f);
		setTo(t);
		setAcyclic(true);
		triggerTypes[0] = CheckResult(appearResult);
		triggerTypes[1] = CheckResult(disappearResult);
		triggerTypes[2] = CheckResult(constantResult);
		CheckType();
	}

	private void CheckType() {
		if (triggerTypes[0] == 'n' && triggerTypes[1] == 'n'
				&& triggerTypes[2] == 'n')
			finalType = "unrelated";
		else if ((triggerTypes[0] == 'c' || triggerTypes[0] == 'n')
				&& (triggerTypes[1] == 'c' || triggerTypes[1] == 'n')
				&& (triggerTypes[2] == 'c' || triggerTypes[2] == 'n'))
			finalType = "complete";
		else
			finalType = "partial";
	}

	private char CheckResult(Map result) {
		if (result == null){
			return 'n';
		}
		else if (!result.isEmpty()) {
			if (result.containsValue(0)) {
				return 'c';
			} else if (result.containsValue(1)) {
				return 'p';
			} else
				return 'u';
		} else
			return 'n';
	}

	public boolean isAcyclic() {
		return acyclic;
	}

	public void setAcyclic(boolean acyclic) {
		this.acyclic = acyclic;
	}

	public HandlerInfo getFrom() {
		return from;
	}

	public void setFrom(HandlerInfo from) {
		this.from = from;
	}

	public HandlerInfo getTo() {
		return to;
	}

	public void setTo(HandlerInfo to) {
		this.to = to;
	}

	public String getFinalType() {
		return finalType;
	}

	public char[] getTriggerTypes() {
		return triggerTypes;
	}
}
