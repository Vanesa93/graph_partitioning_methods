package entity;

import java.util.*;

/**
 * An implentation of the Graph 
 * 
 * @author Vanesa Georgieva
 *
 */

public class Graph {

    private HashMap<Integer, Vertex> vertices;
    private HashMap<Integer, Vertex> namedVertices;    
    private HashMap<Integer, Vertex> indexedVertices;
    private HashMap<Integer, Edge> edges;
     

    public Graph(){
        this.vertices = new HashMap<Integer, Vertex>();
        this.edges = new HashMap<Integer, Edge>();
        this.namedVertices = new HashMap<Integer, Vertex>();
    }
    
    /**
     * @param vertices The initial Vertices to populate this Graph
     */
    public Graph(ArrayList<Vertex> vertices){
    	
        this.vertices = new HashMap<Integer, Vertex>();
        this.edges = new HashMap<Integer, Edge>();       

        for(Vertex v: vertices){
            this.vertices.put(v.getLabel(), v);
        }   

    }  

    /**
     * Add edge to graph with given weight
     */
    public boolean addEdge(Vertex one, Vertex two, int weight, boolean convert) {
    	
        if(one.equals(two)){
            return false;  
        }        
        //ensures the Edge is not in the Graph
        Edge e = new Edge(one, two, weight);
        
        if(edges.containsKey(e.hashCode())){
            return false;
        }
        //and that the Edge isn't already incident to one of the vertices
        
        else if((one.containsNeighbor(e) || two.containsNeighbor(e)) && !convert){
            return false;
        }  
        edges.put(e.hashCode(), e);
        one.addNeighbor(e);
        two.addNeighbor(e);
        one.addNeighborVertex(two);
        two.addNeighborVertex(one);
        return true;
    }
    
    /*
     * check if the graph contains edge
     */
   public boolean containsEdge(Edge e){
        if(e.getOne() == null || e.getTwo() == null){
            return false;
        }      
        return this.edges.containsKey(e.hashCode());
    }
   
    /**
     *  removes the specified Edge from the Graph
     */
    public Edge removeEdge(Edge e){
       e.getOne().removeNeighbor(e);
       e.getTwo().removeNeighbor(e);
       return this.edges.remove(e.hashCode());
    } 

    /**
     *check if the graph contains vertex
     */
    public boolean containsVertex(Vertex vertex){
        return this.vertices.get(vertex.getLabel()) != null;
    }

    /**
     */
    public Vertex getVertex(Integer label){
        return vertices.get(label);
    }
    
    public Vertex getVertexByName(int vertexName){
        return namedVertices.get(vertexName);
    }
    
    public Vertex getVertexByIndex(int vertexIndex){
        return indexedVertices.get(vertexIndex);
    }     

    /**
     * add vertex to the graph
     */
    public boolean addVertex(Vertex vertex, boolean overwriteExisting){
        Vertex current = this.vertices.get(vertex.getLabel());
        if(current != null){
            if(!overwriteExisting){
                return false;
            }           
            while(current.getNeighborCount() > 0){
                this.removeEdge(current.getNeighbor(0));
            }
        }
        
        vertices.put(vertex.getLabel(), vertex);
        namedVertices.put(vertex.getName(), vertex);
        return true;
    }

    /**
     *
     * remove vertex by label
     */
    public Vertex removeVertex(Integer label){
        Vertex v = vertices.remove(label);
        while(v.getNeighborCount() > 0){
            this.removeEdge(v.getNeighbor((0)));
        }
        return v;
    }
    
    /**
     *
     * @return Set<String> The unique labels of the Graph's Vertex objects
     */
    public Set<Integer> vertexKeys(){
        return this.vertices.keySet();
    }
     
    /**
     *
     * @return Set<Edge> The Edges of this graph
     */
    public Set<Edge> getEdges(){
        return new HashSet<Edge>(this.edges.values());
    }
    
    /**
    *
    * @return Set<Vertex> The Vertices of this graph
    */
    public Set<Vertex> getVertices(){
        return new HashSet<Vertex>(this.vertices.values());
    } 
    
    public Set<Vertex> getIndexedVertices(){
        return new HashSet<Vertex>(this.indexedVertices.values());
    } 
    
    /**
    *
    * @return Edge Search for edge in the graph
    */
    public Edge findConnected(Vertex v1, Vertex v2) {
    	Edge edge = new Edge(v1, v2);
        if (!this.getEdges().contains(edge)) 
          return null;
        return edge;
      }
    
    public boolean isConnected(Vertex v1, Vertex v2) {
    	Edge leftEdge = new Edge(v1, v2);
    	Edge rigthEdge = new Edge(v2, v1);
        if (!this.getEdges().contains(leftEdge) || !this.getEdges().contains(rigthEdge)) 
          return false;
        return true;
      }
}

