package plotter;

import java.io.File;
import java.util.regex.Pattern;

import obj.HandlerInfo;
import obj.TransitionInfo;

import org.jgrapht.Graph;

public class Plotter {
	 GraphViz gv;
	 Graph<HandlerInfo, TransitionInfo> graph;
	 public Plotter(Graph g)
	   {   
		   gv = new GraphViz();
		   gv.addln(gv.start_graph());
		   gv.addln("graph [style=bold fontname=\"Bold\" compound=true];" +
		   	"node [shape=record color=black fontname=\"Bold\" fontcolor=blue];" +
		   	"edge [color=black fontname=\"Bold\" fontcolor=red];");
		   graph = g;
		   //VerticesToDOT();
		   //EdgesToDOT();
	   }
	 public void VerticesToDOT(){
		 for (HandlerInfo hi : graph.vertexSet()){
			 if (hi.isAcyclic() == true){
				 System.out.println("expr "+hi.getInputExpr());
				 if(hi.getName().equals("Notification"))
					 gv.add(hi.getName()+"[label=\"<Handler>"+hi.getName()+"|{<Input>Appearance("+Format(hi.getInputExpr())+")|<Output>"+Format(hi.getOutputExpr())+"}\"]");
				 else
					 gv.add(hi.getName()+"[label=\"<Handler>"+hi.getName()+"|{<Input>Appearance("+Format(hi.getInputExpr())+")|<Output>[same]Appearance("+Format(hi.getOutputExpr())+")}\"]");
			 }
			 else {
				 gv.add(hi.getName()+"[color=red label=\"<Handler>"+hi.getName()+"|{<Input>Appearance("+Format(hi.getInputExpr())+")|<Output>[same]Appearance("+Format(hi.getOutputExpr())+"}\"]");
			 }
		 }
	 }
	 
	 public void EdgesToDOT(){
		 for (TransitionInfo ti : graph.edgeSet()){
			 if (ti.isAcyclic() == true){
				 if (ti.getComplete().size() > 0)
					 gv.add(ti.getFrom().getName()+":Output->"+ti.getTo().getName()+
							 ":Input[label=\"c:"+ti.getComplete().size()+" p:"+ti.getPartial().size()+"\"]");
				 else
					 gv.add(ti.getFrom().getName()+":Output->"+ti.getTo().getName()+
							 ":Input[style=dotted label=\"c:"+ti.getComplete().size()+" p:"+ti.getPartial().size()+"\"]");
			 }
			 else {
				 if (ti.getComplete().size() > 0)
					 gv.add(ti.getFrom().getName()+":Output->"+ti.getTo().getName()+
							 ":Input[color=red label=\"c:"+ti.getComplete().size()+" p:"+ti.getPartial().size()+"\"]");
				 else
					 gv.add(ti.getFrom().getName()+":Output->"+ti.getTo().getName()+
							 ":Input[color=red style=dotted label=\"c:"+ti.getComplete().size()+" p:"+ti.getPartial().size()+"\"]");
			 }
		 }
	 }
	 private String Format(String expr){
		 expr = expr.replaceAll("\\|", "\\\\|").replaceAll("\\<", "\\\\<").replaceAll("'\\('", "\\(").replaceAll("'\\)'", "\\)")
				.replaceAll("\\>", "\\\\>").replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}").replaceAll("\"", "");
		 if (expr.length() >= 50){
			 int offset = expr.length()/2+1;
			 if (expr.charAt(offset-1)=='\\')
				 offset++;
			 String newstr = expr.substring(0, offset)+"\\n"+expr.substring(offset, expr.length());
			 return newstr;/////check before cut
		 }
		 else
			 return expr;
	 }
	 public void ExportDot(){
		 String type = "png";
		 File out = new File("plot." + type);
		 gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	 }
}
