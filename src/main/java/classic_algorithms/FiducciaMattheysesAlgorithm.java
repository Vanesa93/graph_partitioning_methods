package classic_algorithms;

import java.util.Comparator;
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

public class FiducciaMattheysesAlgorithm implements IFiducciaMattheysesAlgorithm {

	/** Performs FiducciaMattheyses on the given graph **/
	public FiducciaMattheysesAlgorithm(Graph graph) {
		processGraph(graph);	
		System.out.println("Fiduccia Mattheyses Algorithm");
		System.out.println("Cluster 1");
		System.out.println(getBestPartitionA());
		System.out.println("Cluster 2");
		System.out.println(getBestPartitionB());
	}

	private static VertexGroup partitionA;
	private static VertexGroup partitionB;
	private static VertexGroup bucketA;
	private static VertexGroup bucketB;
	private static Vertex vertexToMove;
	private static HashMap<Integer, Double> vertexCostLabelMap;
	private static HashMap<Double, VertexGroup> maxCostA;
	private static HashMap<Double, VertexGroup> maxCostB;
	
	
	public VertexGroup getBestPartitionA() {
		return partitionA;
	}

	public VertexGroup getBestPartitionB() {
		return partitionB;
	}


	private Graph graph;

	public Graph getGraph() {
		return graph;
	}

	private int partitionSize;
	// vertex group with all unlocked vertices
	private static VertexGroup unLockedVertices;
	
	public void processGraph(Graph g) {
		// initialize variables
		this.graph = g;
		this.partitionSize = g.getVertices().size() / 2;
		bucketA = new VertexGroup();
		bucketB = new VertexGroup();		
		vertexCostLabelMap = new HashMap<Integer, Double>();
		unLockedVertices = new VertexGroup();
		
		// Split vertices into A and B
		setInitialPartition(g);
		// sort vertices by move cost
		Stream<Entry<Integer, Double>> sortedVerticesMap = sortVerticesByCost();		
		Vertex currVertexToMove = this.graph.getVertex(sortedVerticesMap.findFirst().get().getKey());
		// swap first vertex
		swapVertex(currVertexToMove);
		// set gain to current move
		setGain(currVertexToMove);
		vertexToMove = currVertexToMove;
		// do all swaps
		doAllSwaps();	
		// choose best partition
		setBestPartiton();
	}
	
	public void setInitialPartition(Graph g){
		int i = 0;
		for (Vertex v : g.getVertices()) {			
			(++i > partitionSize ? bucketB : bucketA).add(v);
			unLockedVertices.add(v);
		}
			
		maxCostA= new HashMap<Double, VertexGroup>();
		maxCostB= new HashMap<Double, VertexGroup>();
		for (Vertex v : g.getVertices()) {	
			v.setCost(this.getVertexCost(v));
			vertexCostLabelMap.put(v.label, v.cost);
		}	
	}
	
	public Stream<Entry<Integer, Double>> sortVerticesByCost(){
		Stream<Entry<Integer, Double>> sortedMap = vertexCostLabelMap.entrySet().stream().sorted(new Comparator<Entry<Integer, Double>>() {
			public int compare(Entry<Integer, Double> k1, Entry<Integer, Double> k2) {
				return -k1.getValue().compareTo(k2.getValue());
			}
		});
		return sortedMap;
	}
	
	public void doAllSwaps() {
		for(int y=0;y<unLockedVertices.size();y++){			
			for (Vertex v: unLockedVertices) {
				double currentCost = getVertexCost(v);
				v.setCost(currentCost);
			    vertexCostLabelMap.put(v.label, v.cost);
				
			} 
			setVertexToMove();
		    if(unLockedVertices.contains(vertexToMove)){
		    	swapVertex(vertexToMove);
				setGain(vertexToMove);
		    }
		}
	}
	
	public void setVertexToMove(){		
		for(int i = 0; i< vertexCostLabelMap.size(); i++){
			Stream<Entry<Integer, Double>> orderedMap = sortVerticesByCost();
			Vertex v = this.graph.getVertex(orderedMap.findFirst().get().getKey());
			if(bucketA.size()>bucketB.size() && bucketA.contains(v) && unLockedVertices.contains(v)){
	    		vertexToMove = v;
	    	} else if(bucketB.size()>bucketA.size() && bucketB.contains(v) && unLockedVertices.contains(v)){
	    		vertexToMove = v;
	    	} else if(bucketB.size() == bucketA.size() && unLockedVertices.contains(v)){
	    		Stream<Entry<Integer, Double>> currentSortedMap = sortVerticesByCost();
		    	vertexToMove = this.graph.getVertex(currentSortedMap.findFirst().get().getKey());		
	    	} else {
	    		vertexCostLabelMap.remove(v.label);
	    	}
		}	
	}
	
	public void setBestPartiton() {
		Stream<Entry<Double, VertexGroup>> sortedmaxCostA = maxCostA.entrySet().stream().sorted(new Comparator<Entry<Double, VertexGroup>>() {
			public int compare(Entry<Double, VertexGroup> k1, Entry<Double, VertexGroup> k2) {
				return -k1.getKey().compareTo(k2.getKey());
			}
		});
		Stream<Entry<Double, VertexGroup>> sortedmaxCostB = maxCostB.entrySet().stream().sorted(new Comparator<Entry<Double, VertexGroup>>() {
			public int compare(Entry<Double, VertexGroup> k1, Entry<Double, VertexGroup> k2) {
				return -k1.getKey().compareTo(k2.getKey());
			}
		});
		partitionA = sortedmaxCostA.findFirst().get().getValue();
		partitionB = sortedmaxCostB.findFirst().get().getValue();
	}

	public void setGain(final Vertex vertexToMove){
		VertexGroup currentPart = bucketA.contains(vertexToMove) ? bucketA : bucketB;
		VertexGroup oppositePart = bucketA.contains(vertexToMove) ? bucketB : bucketA;
		double currentPartitionBalance = 0;
		for(Vertex v:currentPart){
			for(Edge e : v.getNeighbors()){
				Vertex neighbor = v.equals(e.one) ? e.two :e.one;
				if(oppositePart.contains(neighbor)){
					currentPartitionBalance-=1;
				}
			}			
			
		}
		if((!maxCostA.containsKey(currentPartitionBalance) && !maxCostB.containsKey(currentPartitionBalance)) || currentPart.size() == oppositePart.size()){
			VertexGroup newGroupA = new VertexGroup(currentPart);
			VertexGroup newGroupB = new VertexGroup(oppositePart);
			maxCostA.put(currentPartitionBalance, newGroupA);
			maxCostB.put(currentPartitionBalance, newGroupB);
		}
	}
	
	public void swapVertex(Vertex vertexToMove){
		if (bucketA.contains(vertexToMove)) {
			bucketA.remove(vertexToMove);
			bucketB.add(vertexToMove);
		} else {
			bucketB.remove(vertexToMove);
			bucketA.add(vertexToMove);
		}
		unLockedVertices.remove(vertexToMove);
		vertexCostLabelMap.remove(vertexToMove.label);
	}

	/**
	 * Returns gain for the current vertex. When moving a vertex from within
	 * group A, all internal edges become external edges and vice versa.
	 **/
	
	public double getVertexCost(Vertex v) {
	//  the moving force FS(c) is the number of nets connected 
	//	to c but not connected to any other cells within c’s partition, 
	//	i.e., cut nets that connect only to c, and 
	//	the retention force TE(c) is the number of uncut nets connected to c.
		 double FS = 0;
		   double TE = 0;
		   VertexGroup vGroup = bucketA.contains(v) ? bucketA : bucketB;
		   for (Edge e : v.getNeighbors()) {  
			 Vertex neighbor = v.equals(e.one) ? e.two : e.one;
			 if(!vGroup.contains(neighbor)) FS+=e.weight;
			 if(vGroup.contains(neighbor)) TE+=e.weight;
		   }
		   return FS - TE;
	 }


}