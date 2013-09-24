package eu.visioncloud.workflowengine.plotter;

import java.io.File;
import java.util.regex.Pattern;

import org.jgrapht.Graph;

import eu.visioncloud.workflowengine.obj.HandlerInfo;
import eu.visioncloud.workflowengine.obj.TransitionInfo;

public class Plotter {
	GraphViz gv;
	Graph<HandlerInfo, TransitionInfo> graph;

	public Plotter(Graph g) {
		gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln("graph [style=bold fontname=\"Bold\" compound=true];"
				+ "node [shape=record color=black fontname=\"Bold\" fontcolor=blue];"
				+ "edge [color=black fontname=\"Bold\" fontcolor=red];");
		graph = g;
		// VerticesToDOT();
		// EdgesToDOT();
	}

	public void VerticesToDOT() {
		for (HandlerInfo hi : graph.vertexSet()) {
//			System.out.println("vertex : " + hi.getSlName() + hi.getName() + "[label=\"<Handler>*"
//					+ hi.getSlName() + ":" + hi.getName()
//					+ "|{<Trigger>" + Format(hi.getInputExpr())
//					+ "|<Output>" + Format(hi.getOutputExpr()) + "}\"]");
			if (hi.isDirty()) {
				if (hi.isAcyclic() == true) {
					// System.out.println("expr " + hi.getInputExpr());
					// if(hi.getName().equals("Notification"))
					// gv.add(hi.getName()+"[label=\"<Handler>"+hi.getName()+"|{<Trigger>"+Format(hi.getInputExpr())+"|<Output>"+Format(hi.getOutputExpr())+"}\"]");
					// else
					gv.add("ID"+hi.getSlName() + hi.getName() + "[label=\"<Handler>*"
							+ hi.getSlName() + ":" + hi.getName()
							+ "|{<Trigger>" + Format(hi.getInputExpr())
							+ "|<Output>" + Format(hi.getOutputExpr()) + "}\"]");
				} else {
					gv.add("ID"+hi.getSlName() + hi.getName() + "[color=red label=\"<Handler>*"
							+ hi.getSlName() + ":" + hi.getName()
							+ "|{<Trigger>" + Format(hi.getInputExpr())
							+ "|<Output>" + Format(hi.getOutputExpr()) + "}\"]");
				}
			} else {
				if (hi.isAcyclic() == true) {
					// System.out.println("expr " + hi.getInputExpr());
					// if(hi.getName().equals("Notification"))
					// gv.add(hi.getName()+"[label=\"<Handler>"+hi.getName()+"|{<Trigger>"+Format(hi.getInputExpr())+"|<Output>"+Format(hi.getOutputExpr())+"}\"]");
					// else
					gv.add("ID"+hi.getSlName() + hi.getName() + "[label=\"<Handler>" + hi.getSlName()
							+ ":" + hi.getName() + "|{<Trigger>"
							+ Format(hi.getInputExpr()) + "|<Output>"
							+ Format(hi.getOutputExpr()) + "}\"]");
				} else {
					gv.add("ID"+hi.getSlName() + hi.getName() + "[color=red label=\"<Handler>"
							+ hi.getSlName() + ":" + hi.getName()
							+ "|{<Trigger>" + Format(hi.getInputExpr())
							+ "|<Output>" + Format(hi.getOutputExpr()) + "}\"]");
				}
			}
		}
	}

	public void EdgesToDOT() {
		for (TransitionInfo ti : graph.edgeSet()) {
			if (ti.isAcyclic() == true) {
				if (ti.getFinalType().equals("complete"))
					gv.add("ID"+ti.getFrom().getSlName()+ti.getFrom().getName() + ":Output->"
							+ "ID"+ti.getTo().getSlName()+ti.getTo().getName() + ":Trigger[label=\"app:"
							+ ti.getTriggerTypes()[0] + " dis:"
							+ ti.getTriggerTypes()[1] + " con:"
							+ ti.getTriggerTypes()[2] + "\"]");
				else
					gv.add("ID"+ti.getFrom().getSlName()+ti.getFrom().getName() + ":Output->"
							+ "ID"+ti.getTo().getSlName()+ti.getTo().getName()
							+ ":Trigger[style=dotted label=\"app:"
							+ ti.getTriggerTypes()[0] + " dis:"
							+ ti.getTriggerTypes()[1] + " con:"
							+ ti.getTriggerTypes()[2] + "\"]");
			} else {
				if (ti.getFinalType().equals("complete"))
					gv.add("ID"+ti.getFrom().getSlName()+ti.getFrom().getName() + ":Output->"
							+ "ID"+ti.getTo().getSlName()+ti.getTo().getName()
							+ ":Trigger[color=red label=\"app:"
							+ ti.getTriggerTypes()[0] + " dis:"
							+ ti.getTriggerTypes()[1] + " con:"
							+ ti.getTriggerTypes()[2] + "\"]");
				else
					gv.add("ID"+ti.getFrom().getSlName()+ti.getFrom().getName() + ":Output->"
							+ "ID"+ti.getTo().getSlName()+ti.getTo().getName()
							+ ":Trigger[color=red style=dotted label=\"app:"
							+ ti.getTriggerTypes()[0] + " dis:"
							+ ti.getTriggerTypes()[1] + " con:"
							+ ti.getTriggerTypes()[2] + "\"]");
			}
		}
	}

	private String Format(String expr) {
		expr = expr.replaceAll("\\|", "\\\\|").replaceAll("\\<", "\\\\<")
				.replaceAll("\\>", "\\\\>").replaceAll("\\{", "\\\\{")
				.replaceAll("\\}", "\\\\}").replaceAll("\"", "")
				.replace("disappear", "dis").replace("appear", "app")
				.replace("constant", "con");
		// if (expr.length() >= 50) {
		// int offset = expr.length() / 2 + 1;
		// if (expr.charAt(offset - 1) == '\\')
		// offset++;
		// String newstr = expr.substring(0, offset) + "\\n"
		// + expr.substring(offset, expr.length());
		// return newstr;// ///check before cut
		// } else
		// return expr;
		return expr;
	}

	public void ExportDot() {
		String type = "png";
		File out = new File("plot." + type);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public byte[] ImageByteArray() {
		String type = "png";
		return gv.getGraph(gv.getDotSource(), type);
	}
}
