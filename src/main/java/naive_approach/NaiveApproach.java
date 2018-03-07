package naive_approach;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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

public class NaiveApproach implements INaiveApproach
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
	

    
    /** Performs Naive approach on the given graph **/
    public NaiveApproach(Graph g) throws IOException {
  	  	processGraph(g);
		System.out.println("Naive Approach");
		System.out.println("Cluster 1");
		System.out.println(NaiveApproach.partitionA);
		System.out.println("Cluster 2");
		System.out.println(NaiveApproach.partitionB);
    }
	
	public void processGraph(Graph g) {
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
	
	
	public ArrayList<String> findSplits(ArrayList<Integer> set)  {
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
//	    createFile("allPartitions", output);
	    if(additional != null) {
		    graph.removeVertex(additional.label);
	    }
	    return output;
	}
	public void createFile(String file, ArrayList<String> arrData) {
        FileWriter writer = null;
		try {
			writer = new FileWriter(file + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int size = arrData.size();
        for (int i=0;i<size;i++) {
            String str = arrData.get(i).toString();
            try {
				writer.write(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(i < size-1)
				try {
					writer.write("\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void removeAllDisconnectedPartition() {
		// remove disconnected and set cost;
		int size = potentialPartitionA.size();
		for(int i = 0; i< size & !potentialPartitionA.isEmpty();i++){	
			ArrayList<Integer> currentVertexGroup = potentialPartitionA.get(i);
			// get first node
			allCurentPaths = new ArrayList<Queue<Vertex>>();
			for(int y = 0;y<currentVertexGroup.size()-1;y++){
				Vertex root = graph.getVertex(currentVertexGroup.get(y));
				Vertex end = graph.getVertex(currentVertexGroup.get(y+1));
				serializeUsingBfs(root, end);
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
			int index = (Integer) potentialPartitionA.keySet().toArray()[0];
			ArrayList<Integer> partitionGroupA = new ArrayList<Integer>(potentialPartitionA.get(index));
			ArrayList<Integer> partitionGroupB = new ArrayList<Integer>(potentialPartitionB.get(index));	
			for(int y = 0;y<partitionGroupA.size();y++){
				partitionA.add(graph.getVertex(partitionGroupA.get(y)));
				partitionB.add(graph.getVertex(partitionGroupB.get(y)));
			}
		} else {
			 maxGain = new HashMap<Integer, Double>();
			 for(int i = 0; i< potentialPartitionA.size() & !potentialPartitionA.isEmpty();i++){	
				int index = (Integer) potentialPartitionA.keySet().toArray()[i];
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
			Stream<Entry<Integer, Double>> sortedMap = maxGain.entrySet().stream().sorted(new Comparator<Entry<Integer, Double>>() {
				public int compare(Entry<Integer, Double> k1, Entry<Integer, Double> k2) {
					return -k1.getValue().compareTo(k2.getValue());
				}
			});
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
	
	public double getGain(final Vertex vertex, ArrayList<Integer> currentPartitionA, ArrayList<Integer> currentPartitionB){
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
	
	public void serializeUsingBfs(Vertex root, Vertex end) {
        // BFS uses Queue
        Queue<Vertex> queue = new LinkedList<Vertex>();
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
	
	public Vertex getUnvisitedChildNode(Vertex node, Vertex end, Queue<Vertex> queue) {
		ArrayList<Vertex> allNeighbors = node.neighborhoodVertices;
		if(allNeighbors.contains(end)){
			Queue<Vertex> pathToAdd = new LinkedList<Vertex>(queue);
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
	
	public void clearQueue(Queue<Vertex> queue){
		currentPath = new LinkedList<Vertex>(queue);
		setAllVerticesUnvisited();
		queue.clear();
	}
	
	public void setAllVerticesUnvisited() {
		for(Vertex v: graph.getVertices()){
			v.visited = false;
		}		
	}
}