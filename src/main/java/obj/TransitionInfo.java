package obj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TransitionInfo {
	private HandlerInfo from;
	private HandlerInfo to;
	private ArrayList complete = new ArrayList();
	private ArrayList partial = new ArrayList();
	private ArrayList unrelated = new ArrayList();
	private boolean acyclic;
	public TransitionInfo(HandlerInfo f, HandlerInfo t, Map result){
		setFrom(f);
		setTo(t);
		setAcyclic(true);
		Set s = result.entrySet();
		Iterator i = s.iterator();
		while (i.hasNext()){
			Entry temp = (Entry) i.next();
			if ((Integer)temp.getValue() == 0){
				getComplete().add(temp.getKey());
			}
			else if ((Integer)temp.getValue() == 1){
				getPartial().add(temp.getKey());
			}
			else if ((Integer)temp.getValue() == 2){
				unrelated.add(temp.getKey());
			}
		}
	}
	public boolean isAcyclic() {
		return acyclic;
	}
	public void setAcyclic(boolean acyclic) {
		this.acyclic = acyclic;
	}
	public ArrayList getComplete() {
		return complete;
	}
	public void setComplete(ArrayList complete) {
		this.complete = complete;
	}
	public HandlerInfo getFrom() {
		return from;
	}
	public void setFrom(HandlerInfo from) {
		this.from = from;
	}
	public ArrayList getPartial() {
		return partial;
	}
	public void setPartial(ArrayList partial) {
		this.partial = partial;
	}
	public HandlerInfo getTo() {
		return to;
	}
	public void setTo(HandlerInfo to) {
		this.to = to;
	}
	
}
