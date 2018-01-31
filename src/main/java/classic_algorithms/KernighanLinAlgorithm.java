package classic_algorithms;	

import java.util.LinkedList;

import entity.Edge;
import entity.Graph;
import entity.Vertex;
import entity.VertexGroup;
import helpers.VertexGroupToGraphConverter;
import jsonConverter.ObjectToJson;

/**
 * Kernighan-Lin - splitting a graph into 
 * two groups where the weights of the edges between groups (cutting cost) is minimised
 * @author Vanesa Georgieva
 *
 */

public class KernighanLinAlgorithm implements IKernighanLinAlgorithm {
  
  private static VertexGroup partitionA;
  private static VertexGroup partitionB;
  private VertexGroup unswappedA, unswappedB;
  public static VertexGroup getGroupA() { return partitionA; }
  public static VertexGroup getGroupB() { return partitionB; }
  private Graph graph;
  public Graph getGraph() { return graph; }
  private int partitionSize;
	
  /** Performs KerninghanLin on the given graph **/
  public KernighanLinAlgorithm(Graph g) {
	  	processGraph(g);
	  	
		System.out.println("Kernighan Lin Algorithm");
		System.out.println("Cluster 1");
		System.out.println(getGroupA());
		VertexGroupToGraphConverter.convertVertexGroupToGraph(partitionA);
		System.out.println("Cluster 2");
		System.out.println(getGroupB());
	  	Graph partitionAGraph = VertexGroupToGraphConverter.convertVertexGroupToGraph(partitionA);	
	  	ObjectToJson.convertObjectToJson("D:\\saved\\", "partitionA.json", partitionAGraph);
	  	Graph partitionBGraph = VertexGroupToGraphConverter.convertVertexGroupToGraph(partitionB);
	  	ObjectToJson.convertObjectToJson("D:\\saved\\", "partitionB.json", partitionBGraph);

	  
  }
  
  public void processGraph(Graph g) {
    this.graph = g;
    this.partitionSize = g.getVertices().size() / 2;
    
    partitionA = new VertexGroup();
    partitionB = new VertexGroup();
    // Split vertices into partitionA and partitionB
    int i = 0;
    for (Vertex v : g.getVertices()) {
      (++i > partitionSize ? partitionB : partitionA).add(v);
    }
    // create new unSwapped groups for processing
    unswappedA = new VertexGroup(partitionA);
    unswappedB = new VertexGroup(partitionB);
    
    doAllSwaps();
  }
  
  /** Performs swaps(half of graph vertices) and chooses the one with least cut cost one **/
  public void doAllSwaps() {

    LinkedList<Edge> swaps = new LinkedList<Edge>();
    double minCost = Double.POSITIVE_INFINITY;
    int minId = -1;
    
    for (int i = 0; i < partitionSize; i++) {
      double cost = doSingleSwap(swaps);
      if (cost < minCost) {
        minCost = cost; 
        minId = i; 
      }
    }
    // Unwind swaps
    while (swaps.size()-1 > minId) {
      Edge pair = swaps.pop();
      // unswap
      swapVertices(partitionA, pair.two, partitionB, pair.one);
    }
  }
  
  /** Chooses the least cost swap and performs it **/
  public double doSingleSwap(LinkedList<Edge> swaps) {
   
    Edge maxPair = null;
    double maxGain = Double.NEGATIVE_INFINITY;    
    for (Vertex v_a : unswappedA) {
      for (Vertex v_b : unswappedB) {        
        Edge e = graph.findConnected(v_a, v_b);
        double edge_cost = (e != null) ? e.weight : 0;
        // Calculate the gain in cost if these vertices were swappeds
        double gain = getVertexCost(v_a) + getVertexCost(v_b) - 2 * edge_cost;
        
        if (gain > maxGain) {
          maxPair = new Edge(v_a, v_b);
          maxGain = gain;
        }
      }
    }
    
    swapVertices(partitionA, maxPair.one, partitionB, maxPair.two);
    swaps.push(maxPair);
    unswappedA.remove(maxPair.one);
    unswappedB.remove(maxPair.two);
    
    return getCutCost();
  }

  /** Returns the difference of external cost and internal cost of this vertex.
   *  When moving a vertex from within group A, all internal edges become external 
   *  edges and the opposite **/
  public double getVertexCost(Vertex v) {
    
    double cost = 0;

    boolean v1isInA = partitionA.contains(v);
    
    for (Vertex v2 : graph.getVertices()) {      
      boolean v2isInA = partitionA.contains(v2);
      Edge edge = graph.findConnected(v, v2);
      double edge_cost = (edge != null) ? edge.weight : 0;
      if (v1isInA != v2isInA) // external
        cost += edge_cost;
      else
        cost -= edge_cost;
    }
    return cost;
  }
  
  /** Returns the sum of the costs of all edges between A and B **/
  public double getCutCost() {
    double cost = 0;
    for (Edge edge : graph.getEdges()) {     
      boolean firstInA = partitionA.contains(edge.one);
      boolean secondInA= partitionA.contains(edge.two);
      if (firstInA != secondInA) // external
        cost += edge.weight;
    }
    return cost;
  }
  
  /** Swaps va and vb in groups a and b **/
  public void swapVertices(VertexGroup a, Vertex va, VertexGroup b, Vertex vb) {
    if (!a.contains(va) || a.contains(vb) ||
        !b.contains(vb) || b.contains(va)) throw new RuntimeException("Invalid swap");
    a.remove(va); a.add(vb);
    b.remove(vb); b.add(va);
  }
}