package graph;

import java.util.HashSet;

/**
 * An implentation of the Group of Graph Vertices
 * 
 * @author Vanesa Georgieva
 *
 */

public class VertexGroup extends HashSet<Vertex> {  
	private static final long serialVersionUID = -5366673520751513159L;
	public VertexGroup(HashSet<Vertex> clone) { super(clone); }
	public VertexGroup() { }
}