package obj;

public class Attribute {
private String id;
private String key;
private String operator;
private String value;

public Attribute(String id, String k, String o, String v){
	this.setKey(k);
	this.setOperator(o);
	this.setValue(v);
	this.setId(id);
}

public String getKey() {
	return key;
}

public void setKey(String key) {
	this.key = key;
}

public String getOperator() {
	return operator;
}

public void setOperator(String operator) {
	this.operator = operator;
}

public String getValue() {
	return value;
}

public void setValue(String value) {
	this.value = value;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}
}
