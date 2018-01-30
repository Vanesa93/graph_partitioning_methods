package entity;

import java.util.ArrayList;

/**
 * An implentation of the Graph Vertex
 * 
 * @author Vanesa Georgieva
 *
 */

public class Vertex {

    public ArrayList<Edge> neighborhood;
    public ArrayList<Vertex> neighborhoodVertices;
    public Integer label;
    public Integer name;
    public double cost;
    public boolean visited;
  
   /**
     *
     * @param label The unique label associated with this Vertex
     */

    public Vertex(int label){
       this.label = label;
       this.neighborhood = new ArrayList<Edge>();
       this.neighborhoodVertices = new ArrayList<Vertex>();
       this.visited = false;
    }
    
    public Vertex(int label, int name){
        this.label = label;
        this.name = name;
        this.neighborhood = new ArrayList<Edge>();
        this.neighborhoodVertices = new ArrayList<Vertex>();
        this.visited = false;
     }
   
    /**
     * @param edge The edge to add
     *  add edge to the neighborhood
     */

    public void addNeighbor(Edge edge){
       if(this.neighborhood.contains(edge)){
            return;
        }      
        this.neighborhood.add(edge);
    }
    
    public void addNeighborVertex(Vertex vertex){
        if(this.neighborhoodVertices.contains(vertex)){
             return;
         }      
         this.neighborhoodVertices.add(vertex);
     }
    
    /**
     *
     * @param search for neighbor edge
     * @return if the other is in the neighborhood true
     */

    public boolean containsNeighbor(Edge other){
        return this.neighborhood.contains(other);
    }
    
    /**
     *
     * @param index The index of the Edge to retrieve
     * @return get specified edge by index
     */    
    
    public Edge getNeighbor(int index){
    	return this.neighborhood.get(index);
    }
    
    /**
     *
     * @param index The index of the edge to remove from this.neighborhood
     * @return Edge The removed Edge
     */

    Edge removeNeighbor(int index){
       return this.neighborhood.remove(index);
    }
    
    /**
     *
     * @param e The Edge to remove from this.neighborhood
     */
    
    public void removeNeighbor(Edge e){
        this.neighborhood.remove(e);
   }
    
    /**
     *
     * @return int The number of neighbors of this Vertex
     */

  public int getNeighborCount(){
        return this.neighborhood.size();
    }
  
  /**
    *
    * @return String The label of this Vertex
    */

    public Integer getLabel(){
        return this.label;
    }
    
    public Integer getName(){
        return this.name;
    }
    
    public double setCost(double cost){
        return this.cost = cost;
    }
    
    public double getCost(){
        return this.cost;
    }
    
    /**
     *
     * @return represents the vertex as string
     */

    public String toString(){
        return "Vertex " + label.toString();
    }

     

    /**
     *
     * @return The hash code of this Vertex's label
     */

   public int hashCode(){
        return this.label.hashCode();
    }

     

   /**
     *
     * @param other The object to compare
     * @return compare two vertex by label
  */

    public boolean equals(Object other){
        if(!(other instanceof Vertex)){
            return false;
     }     
        Vertex v = (Vertex)other;
        return this.label.equals(v.label);
    }

     

    /**
     *
     * @return ArrayList<Edge> A copy of this.neighborhood. 
     */

    public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(this.neighborhood);
    }
    
    public int[] getNeighborsVertices(){
    	int [] neighborhood = new int[this.neighborhood.size()];
    	int i = 0;
    	for(Edge e : this.neighborhood){
    		if(!e.one.getLabel().equals(this.label)){
    			neighborhood[i] = e.one.getLabel();
    		} else {
    			neighborhood[i] = e.two.getLabel();
    		}
    		i++;
    	}
        return neighborhood;
    }

     

}
