package classic_algorithms;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Stream;

import entity.Edge;
import entity.Graph;
import entity.Vertex;
import entity.VertexGroup;

/**
 * An implentation of the Fiduccia Mattheyses Algorithm
 * 
 * @author Vanesa Georgieva
 *
 */

public interface IFiducciaMattheysesAlgorithm  {
	
	VertexGroup getBestPartitionA();
	VertexGroup getBestPartitionB();
	Graph getGraph();	
	void processGraph(Graph g);	
	void setInitialPartition(Graph g);	
	Stream<Entry<Integer, Double>> sortVerticesByCost();
	void doAllSwaps() ;
	void setVertexToMove();	
	void setBestPartiton();
	void setGain(final Vertex vertexToMove);	
	void swapVertex(Vertex vertexToMove);
	double getVertexCost(Vertex v);
}