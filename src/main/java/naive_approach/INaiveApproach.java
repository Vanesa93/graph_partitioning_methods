package naive_approach;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.stream.Stream;

import entity.Edge;
import entity.Graph;
import entity.Vertex;
import entity.VertexGroup;

/**
 * An implentation of the Naive approach for graph partitioning
 * 
 * @author Vanesa Georgieva
 *
 */

public interface INaiveApproach 
{
	void processGraph(Graph g) throws IOException;	
    ArrayList<String> findSplits(ArrayList<Integer> set) throws IOException;
	void createFile(String file, ArrayList<String> arrData);
    void removeAllDisconnectedPartition(); 
	double getGain(final Vertex vertex, ArrayList<Integer> currentPartitionA, ArrayList<Integer> currentPartitionB);
	void serializeUsingBfs(Vertex root, Vertex end);
	Vertex getUnvisitedChildNode(Vertex node, Vertex end, Queue<Vertex> queue);
	void clearQueue(Queue<Vertex> queue);	
	void setAllVerticesUnvisited();
}