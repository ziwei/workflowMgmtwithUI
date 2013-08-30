package obj;

import java.util.Comparator;

public class AttrComparator implements Comparator<Attribute> {

	@Override
	public int compare(Attribute arg0, Attribute arg1) {
		// TODO Auto-generated method stub
		int length0 = (arg0.getKey()+arg0.getOperator()+arg0.getValue()).length();
		int length1 = (arg1.getKey()+arg1.getOperator()+arg1.getValue()).length();
		return length0 > length1 ? -1 : length0 == length1 ? 0 : 1;
	}
}
