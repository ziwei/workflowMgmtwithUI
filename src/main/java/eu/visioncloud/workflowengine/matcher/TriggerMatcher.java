/*
 * Entry point of workflow analysis engine, loop for any two trigger and output expressions
 * , detect cycles and plot to image files
 */
package eu.visioncloud.workflowengine.matcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orbital.logic.imp.Formula;
import orbital.logic.sign.ParseException;

import java.util.regex.*;

import org.apache.log4j.Logger;
import org.jgrapht.GraphPath;

import eu.visioncloud.workflowengine.obj.EvalConstruct;
import eu.visioncloud.workflowengine.obj.HandlerInfo;
import eu.visioncloud.workflowengine.obj.TransitionInfo;
import eu.visioncloud.workflowengine.plotter.Plotter;

public class TriggerMatcher {
	private  final static Logger logger = Logger.getLogger("matcher");
	public static byte[] TriggerMatch(Set<HandlerInfo> handlers) {
		// int NUMBER = 5;
		// TODO Auto-generated method stub
		// ExpressionMatcher
		ExpressionMatcher em = new ExpressionMatcher();
		GraphGenerator gg = new GraphGenerator();
		Plotter plotter = new Plotter(gg.graph);
		
		// List<HandlerInfo> handlers = LoadHandlers();
		gg.GenVertices(handlers);
		for (HandlerInfo from : handlers) {
			for (HandlerInfo to : handlers) {
				// System.out.println("From " + from.getName() + " To "
				// + to.getName());
				Map appearResult = MatchExpr(from.getAppear(), to.getAppear(),
						em);
				Map disappearResult = MatchExpr(from.getDisappear(),
						to.getDisappear(), em);
				Map constantResult = MatchExpr(from.getConstant(),
						to.getConstant(), em);
				// System.out.println("Original Output: " +
				// from.getOutputExpr());
				// System.out.println("Dictionary: " + from.getAtoms());
				// System.out.println("Disjunctive Output: "
				// + em.DNFTransfer(from.getNewoExpr()));
				// System.out.println(result);
				Map result = new HashMap();
				if (appearResult != null)
					result.putAll(appearResult);
				if (disappearResult != null)
					result.putAll(disappearResult);
				if (constantResult != null)
					result.putAll(constantResult);
				if (result.containsValue(0) || result.containsValue(1)) {
					gg.GenEdges(from, to, new TransitionInfo(from, to,
							appearResult, disappearResult, constantResult));
				}
			}
		}
		// gg.ExportDot();
		List cycles = gg.CycleDetection();
		// System.out.println(cycles);
		plotter.VerticesToDOT();
		plotter.EdgesToDOT();
		// plotter.ExportDot();
		return plotter.ImageByteArray();
		// List<GraphPath> lPaths = gg.getPaths(handlers.get(0),
		// handlers.get(1));
		// for (GraphPath<HandlerInfo, TransitionInfo> gpl : lPaths){
		// for (TransitionInfo ti : gpl.getEdgeList())
		// System.out.print(ti.getFrom().getName()+"->"+ti.getTo().getName()+" ");
		// System.out.println();
		// }
	}

	public static void TriggerDisplay(Map<String, Integer> triggers) {

		Set<String> keyset = triggers.keySet();
		Iterator<String> i = keyset.iterator();
		while (i.hasNext()) {
			String subExpr = i.next();
			switch (triggers.get(subExpr)) {
			case 0:
				System.out.println(subExpr + " is a complete trigger");
				break;
			case 1:
				System.out.println(subExpr + " is a partial trigger");
				break;
			case 2:
				System.out.println(subExpr + " is not a trigger");
				break;
			default:
				break;
			}
		}
	}

	public static Map MatchExpr(EvalConstruct from, EvalConstruct to,
			ExpressionMatcher em) {
		if (from != null && to != null) {
			try {
				Formula[] axioms = em.AxiomsGen(from.getOutputAtoms(),
						to.getInputAtoms());
				return em.Prove(axioms, from.getNewoExpr(), to.getNewiExpr());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logger.info("IllegalArgument ",e);
				return null;
			}
		} else
			return null;

	}
	// public static List<HandlerInfo> LoadHandlers() throws IOException{
	// BufferedReader br;
	// String name;
	// String inputExpr;
	// String outputExpr;
	// List<HandlerInfo> handlers = new ArrayList();
	// //for (int i = 1; i <= num; ++i){
	// br = new BufferedReader(new FileReader("test/1.txt"));
	// //System.out.println("OK till here" + num);
	// while ((name = br.readLine()) != null){
	// inputExpr = br.readLine();
	// outputExpr = br.readLine();
	// handlers.add(new HandlerInfo(name, inputExpr, outputExpr));
	// br.readLine();
	// }
	// //System.out.println("OK till here");
	// br.close();
	//
	// //}
	// return handlers;
	// }
}
