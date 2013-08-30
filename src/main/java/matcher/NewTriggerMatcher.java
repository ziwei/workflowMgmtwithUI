package matcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import obj.HandlerInfo;
import obj.TransitionInfo;
import orbital.logic.imp.Formula;
import orbital.logic.sign.ParseException;

import java.util.regex.*;

import org.jgrapht.GraphPath;

import plotter.Plotter;

public class NewTriggerMatcher {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IllegalArgumentException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, ParseException, IOException {
		//int NUMBER = 5;
		// TODO Auto-generated method stub
		ExpressionMatcher em = new ExpressionMatcher();
		GraphGenerator gg = new GraphGenerator();
		Plotter plotter = new Plotter(gg.graph);
		Set<HandlerInfo> handlers = LoadHandlers();
		gg.GenVertices(handlers);
		for (HandlerInfo from : handlers){
			for (HandlerInfo to : handlers){
				System.out.println("From " + from.getName() + " To " + to.getName());
				Map result = MatchExpr(from, to, em);
				System.out.println("Original Output: " + from.getOutputExpr());
				System.out.println("Dictionary: " + from.getAtoms());
				System.out.println("Disjunctive Output: " + em.DNFTransfer(from.getNewoExpr()));
				//System.out.println(result);
				if (result.containsValue(0)||result.containsValue(1)){
					gg.GenEdges(from, to, new TransitionInfo(from, to, result));
				}
				TriggerDisplay(result);
				System.out.println();
			}
		}
		//gg.ExportDot();
		//List cycles = gg.CycleDetection();
		//System.out.println(cycles);
		plotter.VerticesToDOT();
		plotter.EdgesToDOT();
		plotter.ExportDot();
		
//		List<GraphPath> lPaths = gg.getPaths(handlers.get(0), handlers.get(1));
//		for (GraphPath<HandlerInfo, TransitionInfo> gpl : lPaths){
//			for (TransitionInfo ti : gpl.getEdgeList())
//				System.out.print(ti.getFrom().getName()+"->"+ti.getTo().getName()+" ");
//			System.out.println();
//		}
	}
	
	public static void TriggerDisplay(Map<String, Integer> triggers){
		
		Set<String> keyset = triggers.keySet();
		Iterator<String> i = keyset.iterator();
		while (i.hasNext()){
			String subExpr = i.next();
			switch(triggers.get(subExpr)){
			case 0: System.out.println(subExpr + " is a complete trigger");break;
			case 1: System.out.println(subExpr + " is a partial trigger");break;
			case 2: System.out.println(subExpr + " is not a trigger");break;
			default: break;
			}
		}
	}
	public static Map MatchExpr(HandlerInfo handlerFrom, HandlerInfo handlerTo, ExpressionMatcher em){
		try {
			Formula[] axioms = em.AxiomsGen(handlerFrom.getOutputAtoms(), handlerTo.getInputAtoms());
			return em.Prove(axioms, handlerFrom.getNewoExpr(), handlerTo.getNewiExpr());
		} catch (IllegalArgumentException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}//This is to extract 
		
	}
	public static Set<HandlerInfo> LoadHandlers() throws IOException{
		BufferedReader br;
		String name;
		String inputExpr;
		String outputExpr;
		Set<HandlerInfo> handlers = new HashSet<HandlerInfo>();
		//for (int i = 1; i <= num; ++i){
			br = new BufferedReader(new FileReader("test/1.txt"));
			//System.out.println("OK till here" + num);
			while ((name = br.readLine()) != null){
				inputExpr = br.readLine();
				outputExpr = br.readLine();
				handlers.add(new HandlerInfo(name, inputExpr, outputExpr));
				br.readLine();
			}
			//System.out.println("OK till here");
			br.close();
		
		//}
		return handlers;
	}
}
