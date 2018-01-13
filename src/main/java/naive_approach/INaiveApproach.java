package naive_approach;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.stream.Stream;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import graph.VertexGroup;

/**
 * An implentation of the Naive approach for graph partitioning
 * 
 * @author Vanesa Georgieva
 *
 */

public class NaiveApproach 
{
	static int N;
	static Graph graph;
	public static VertexGroup partitionA;
	public static VertexGroup partitionB;
	private static HashMap<Integer, ArrayList<Integer>> potentialPartitionA;
	private static HashMap<Integer, ArrayList<Integer>> potentialPartitionB;
	private static Queue<Vertex> currentPath;
	private static ArrayList<Queue<Vertex>> allCurentPaths;
	private static HashMap<Integer, Double> maxGain;
    public VertexGroup getGroupA() { return partitionA; }
    public VertexGroup getGroupB() { return partitionB; }
    static Vertex additional;
	

    
    /** Performs KerninghanLin on the given graph **/
    public NaiveApproach(Graph g) {
  	  	try {
			processGraph(g);
			System.out.println("Naive Approach");
	  		System.out.println("Cluster 1");
	  		System.out.println(NaiveApproach.partitionA);
	  		System.out.println("Cluster 2");
	  		System.out.println(NaiveApproach.partitionB);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void processGraph(Graph g) throws IOException {
		 graph = g;
		 N = graph.getVertices().size();

		 if(graph.getVertices().size()%2 ==1){
			 int additionalVertexLabel = graph.getVertices().size();
			 additional = new Vertex(additionalVertexLabel);
			 graph.addVertex(additional, false);
		 } 
			
		 ArrayList<Integer> list=new ArrayList<Integer>();
	    	for(int i=0;i<graph.getVertices().size();i++){
	 	 		list.add(graph.getVertex(i).label);
		 	}
	    	findSplits(list);		     
		    removeAllDisconnectedPartition();
	}
	
	
	private static ArrayList<String> findSplits(ArrayList<Integer> set) throws IOException {
	    ArrayList<String> output = new ArrayList<String>();
	    ArrayList<Integer> first = new ArrayList<Integer>(), second = new ArrayList<Integer>();
	    potentialPartitionA = new HashMap<Integer, ArrayList<Integer>>();
	    potentialPartitionB = new HashMap<Integer, ArrayList<Integer>>();
	    String bitString;
	    int bits = (int) Math.pow(2, set.size());
	    int positionPos = 0;
	    while (bits-- > 0) {
	        bitString = String.format("%" + set.size() + "s", Integer.toBinaryString(bits)).replace(' ', '0');
	        for (int i = 0; i < set.size(); i++) {
	            if (bitString.substring(i, i+1).equals("0")) {
	                first.add(set.get(i));
	            } else {
	                second.add(set.get(i));
	            }
	        }
	        if (first.size() < set.size() && second.size() < set.size() && first.size() == second.size()) {
	            if (!output.contains(first + " " + second) && !output.contains(second + " " + first)) {
	            	// remove the additional vertex, if there is one
	            	if(N%2==1){
	            		if(first.contains(additional.label)){
		            		first.remove(additional.label);
		            	}
		            	if(second.contains(additional.label)){
		            		second.remove(additional.label);
		            	}
	            	}
	            	ArrayList<Integer> currentFirst = new ArrayList<Integer>(first);
	            	ArrayList<Integer> currentSecond = new ArrayList<Integer>(second);
	            	potentialPartitionA.put(positionPos,currentFirst);
	            	potentialPartitionB.put(positionPos,currentSecond);
	            	output.add(first + " " + second);
	            	positionPos+=1;
	            }
	        }
	       
	        first.clear();
	        second.clear();
	    }	    
	    createFile("allPartitions", output);
	    if(additional != null) {
		    graph.removeVertex(additional.label);
	    }
	    return output;
	}
	private static void createFile(String file, ArrayList<String> arrData)
            throws IOException {
        FileWriter writer = new FileWriter(file + ".txt");
        int size = arrData.size();
        for (int i=0;i<size;i++) {
            String str = arrData.get(i).toString();
            writer.write(str);
            if(i < size-1)//This prevent creating a blank like at the end of the file**
                writer.write("\n");
        }
        writer.close();
    }
	
	private static void removeAllDisconnectedPartition() {
		// remove disconnected and set cost;
		int size = potentialPartitionA.size();
		for(int i = 0; i< size & !potentialPartitionA.isEmpty();i++){	
			ArrayList<Integer> currentVertexGroup = potentialPartitionA.get(i);
			// get first node
			allCurentPaths = new ArrayList<Queue<Vertex>>();
			for(int y = 0;y<currentVertexGroup.size()-1;y++){
				Vertex root = graph.getVertex(currentVertexGroup.get(y));
				Vertex end = graph.getVertex(currentVertexGroup.get(y+1));
				serialize_using_bfs(root, end);
				currentPath.clear();
			}
			for(int y=0;y<allCurentPaths.size();y++){
				Queue<Vertex> current = allCurentPaths.get(y);
				for(Vertex v : current){
					if(!currentVertexGroup.contains(v.label)){
						currentVertexGroup.clear();
						potentialPartitionA.remove(i);
						potentialPartitionB.remove(i);
					}
				}
			}
			allCurentPaths.clear();
		}
		
		partitionA = new VertexGroup();
		partitionB = new VertexGroup();
		
		// if there is only one group lefr
		if(potentialPartitionA.size() == 1){
			int index = (int) potentialPartitionA.keySet().toArray()[0];
			ArrayList<Integer> partitionGroupA = new ArrayList<Integer>(potentialPartitionA.get(index));
			ArrayList<Integer> partitionGroupB = new ArrayList<Integer>(potentialPartitionB.get(index));	
			for(int y = 0;y<partitionGroupA.size();y++){
				partitionA.add(graph.getVertex(partitionGroupA.get(y)));
				partitionB.add(graph.getVertex(partitionGroupB.get(y)));
			}
		} else {
			 maxGain = new HashMap<Integer, Double>();
			 for(int i = 0; i< potentialPartitionA.size() & !potentialPartitionA.isEmpty();i++){	
				int index = (int) potentialPartitionA.keySet().toArray()[i];
				ArrayList<Integer> currentPartitionA = potentialPartitionA.get(index);
				ArrayList<Integer> currentPartitionB = potentialPartitionB.get(index);
				double currentPartitionGain = 0;
				for(int y = 0;y<currentPartitionA.size()-1;y++){
					Vertex currentVertex = graph.getVertex(currentPartitionA.get(y));
					double currentVertexGain = getGain(currentVertex, currentPartitionA, currentPartitionB);
					currentPartitionGain += currentVertexGain;
				}
				maxGain.put(index, currentPartitionGain);
			}	
			Stream<Entry<Integer, Double>> sortedMap = maxGain.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()));
	        int bestPartitionIndex = sortedMap.findFirst().get().getKey();
	    	ArrayList<Integer> partitionGroupA = new ArrayList<Integer>(potentialPartitionA.get(bestPartitionIndex));
			ArrayList<Integer> partitionGroupB = new ArrayList<Integer>(potentialPartitionB.get(bestPartitionIndex));	
			for(int y = 0;y<partitionGroupA.size();y++){
				partitionA.add(graph.getVertex(partitionGroupA.get(y)));
			}
			for(int y = 0;y<partitionGroupB.size();y++){
				partitionB.add(graph.getVertex(partitionGroupB.get(y)));
			}
		}
	}
	
	private static double getGain(final Vertex vertex, ArrayList<Integer> currentPartitionA, ArrayList<Integer> currentPartitionB){
		ArrayList<Integer> oppositePart = currentPartitionA.contains(vertex.label) ? currentPartitionB : currentPartitionA;
		double currentVertexGain = 0;
		for(Edge e : vertex.getNeighbors()){
				Vertex neighbor = vertex.equals(e.one) ? e.two :e.one;
				if(oppositePart.contains(neighbor.label)){
					currentVertexGain-=1;
				}
		}	
		return currentVertexGain;
	}
	
	public static void serialize_using_bfs(Vertex root, Vertex end) {
        // BFS uses Queue
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(root);
    	Vertex node = root;  
        while (!queue.isEmpty()) {
            Vertex child = null;
            while ((child = getUnvisitedChildNode(node, end, queue)) != null) {
                child.visited = true;
                node = child;
                queue.add(child);
            }
        }
    }
	
	private static Vertex getUnvisitedChildNode(Vertex node, Vertex end, Queue<Vertex> queue) {
		ArrayList<Vertex> allNeighbors = node.neighborhoodVertices;
		if(allNeighbors.contains(end)){
			Queue<Vertex> pathToAdd = new LinkedList<>(queue);
			queue.add(end);
			allCurentPaths.add(pathToAdd);
			clearQueue(queue);
			return null;
		}
		for(Vertex v: allNeighbors){
			if(v.visited == false){
				return v;
			}
		}
		clearQueue(queue);
        return null;
    }
	
	private static void clearQueue(Queue<Vertex> queue){
		currentPath = new LinkedList<>(queue);
		setAllVerticesUnvisited();
		queue.clear();
	}
	
	private static void setAllVerticesUnvisited() {
		for(Vertex v: graph.getVertices()){
			v.visited = false;
		}		
	}
}