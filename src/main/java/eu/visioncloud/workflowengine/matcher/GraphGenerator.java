/*
 * Generate Vertices and Edges, detect possible cycles
 */
package eu.visioncloud.workflowengine.matcher;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

//import org.jgraph.graph.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.StringEdgeNameProvider;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;
//import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.alg.StrongConnectivityInspector;

import eu.visioncloud.workflowengine.obj.HandlerInfo;
import eu.visioncloud.workflowengine.obj.TransitionInfo;

public class GraphGenerator {
	DirectedGraph<HandlerInfo, TransitionInfo> graph;

	public GraphGenerator() {
		graph = new DefaultDirectedGraph<HandlerInfo, TransitionInfo>(
				TransitionInfo.class);
	}

	public void GenVertices(Set<HandlerInfo> handlers) {
		// DirectedGraph<String, DefaultEdge> g = new
		// DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		for (HandlerInfo hi : handlers) {
			graph.addVertex(hi);
			//System.out.println(hi.getSlName()+hi.getName());
		}
	}

	public void GenEdges(HandlerInfo s, HandlerInfo t, TransitionInfo ti) {
		graph.addEdge(s, t, ti);
	}

	// public void ExportDot() throws IOException{
	// Writer w = new FileWriter("text.dot");
	// VertexNameProvider labelProvider = new StringNameProvider<String>();
	// VertexNameProvider idProvider = new IntegerNameProvider<String>();
	// StringEdgeNameProvider<String> enp = new StringEdgeNameProvider();
	// DOTExporter d = new DOTExporter(idProvider, labelProvider, enp);
	// d.export(w, graph);
	// }
	public List CycleDetection() {
		StrongConnectivityInspector sci = new StrongConnectivityInspector(graph);
		List<DirectedSubgraph> subgs = sci.stronglyConnectedSubgraphs();
		for (DirectedSubgraph<HandlerInfo, TransitionInfo> ds : subgs) { // /
																			// reference
																			// modified?
			if (ds.edgeSet().size() > 0) {
				for (TransitionInfo ti : ds.edgeSet()) {
					// System.out.println(ti.from.name+"->"+ti.to.name);
					ti.setAcyclic(false);
				}
				for (HandlerInfo hi : ds.vertexSet()) {
					// System.out.println(hi.name);
					hi.setAcyclic(false);
				}
			}
		}
		// CycleDetector cd = new CycleDetector(graph);
		return subgs;
	}

	public List<GraphPath> getPaths(HandlerInfo start, HandlerInfo end) {
		KShortestPaths ksp = new KShortestPaths(graph, start, 8);
		return ksp.getPaths(end);
	}
}
