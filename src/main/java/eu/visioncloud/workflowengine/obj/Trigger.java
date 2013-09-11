package eu.visioncloud.workflowengine.obj;

import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


@XmlRootElement
public final class Trigger {

	public static Trigger[] createTriggers(String input)
		{
		System.out.println(input);
		Trigger[] res = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			res = mapper.readValue(input, Trigger[].class);
		} catch (JsonParseException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}

		// if no return value try to unexcape things
		if (res == null) {
			input = jsonUnEscape(input);
			try {
				res = mapper.readValue(input, Trigger[].class);
			} catch (JsonParseException e) {
			} catch (JsonMappingException e) {
			} catch (IOException e) {
				
			}

		}
		System.out.println(res);
		return res;
	}

	public String getHandlerID() {
		return handlerID;
	}

	public void setHandlerID(String handlerID) {
		this.handlerID = handlerID;
	}

	public boolean isTriggerOnReplica() {
		return triggerOnReplica;
	}

	public void setTriggerOnReplica(boolean triggerOnReplica) {
		this.triggerOnReplica = triggerOnReplica;
	}

	public String getTriggerEvaluator() {
		return triggerEvaluator;
	}

	public void setTriggerEvaluator(String triggerEvaluator) {
		this.triggerEvaluator = triggerEvaluator;
	}


	public String[] getTriggerTypes() {
		return triggerTypes;
	}

	public void setTriggerTypes(String[] triggerTypes) {
		this.triggerTypes = triggerTypes;
	}

	private String handlerID = null;
	private String[] triggerTypes = null;
	private boolean executeLocal = false;
	private boolean triggerOnReplica = false;
	private String triggerEvaluator = null;
	private String outputEvaluator = null;

	/**
	 * Test if Event matches Trigger conditions
	 */

	
	public boolean isExecuteLocal() {
		return executeLocal;
	}

	public void setExecuteLocal(boolean executeLocal) {
		this.executeLocal = executeLocal;
	}


	public String getOutputEvaluator() {
		return outputEvaluator;
	}

	public void setOutputEvaluator(String outputEvaluator) {
		this.outputEvaluator = outputEvaluator;
	}
	
	public static String jsonUnEscape(String string){
	      
        string = string.replace("\\b", "\b");
        string = string.replace("\\t", "\t");
        string = string.replace("\\n", "\n");
        string = string.replace("\\f", "\f");
        string = string.replace("\\r", "\r");
  
		char         c1 = 0;
		int          i;
		int          len = string.length();
		StringBuffer sb = new StringBuffer(len);
		boolean removed = false;
		for (i = 0; i < len; i += 1) {
			c1 = string.charAt(i);
			if(c1=='\\' && !removed){
				removed = true;
			}else{
				sb.append(c1);
				removed = false;
			}
		}
		return sb.toString();
}
}
