package helpers;

import java.util.Set;

import entity.Edge;
import entity.Graph;
import entity.Vertex;
import entity.VertexGroup;

public class VertexGroupToGraphConverterService {

	public static Graph convertVertexGroupToGraph(VertexGroup vertexGroup) {
		
		Graph graph = new Graph();
		for (Vertex v : vertexGroup) {
		    graph.addVertex(v, false);		   
		}
		Set<Vertex> vertices = graph.getVertices();
		for (Vertex v : vertices) {
			for(Edge e : v.getNeighbors()) {
			    Vertex neighbour = e.one.label == v.label ? e.two : e.one;
			    if(vertices.contains(neighbour)) {
			    	graph.addEdge(e.one,e.two,e.weight, true);
			    }
			}
		}		
		return graph;		
	}
}
