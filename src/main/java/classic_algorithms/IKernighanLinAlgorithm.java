package classic_algorithms;	

import java.util.LinkedList;

import entity.Edge;
import entity.Graph;
import entity.Vertex;
import entity.VertexGroup;

/**
 * Kernighan-Lin - splitting a graph into 
 * two groups where the weights of the edges between groups (cutting cost) is minimised
 * @author Vanesa Georgieva
 *
 */

public interface IKernighanLinAlgorithm {
  
  void processGraph(Graph g); 
  void doAllSwaps();
  double doSingleSwap(LinkedList<Edge> swaps);
  double getVertexCost(Vertex v);
  double getCutCost();
  void swapVertices(VertexGroup a, Vertex va, VertexGroup b, Vertex vb);
}