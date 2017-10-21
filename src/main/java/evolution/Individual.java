package evolution;

import java.util.*;

import graph.Graph;

/**
 * An implentation of Individual used for evolutionary approach
 * 
 * @author Vanesa Georgieva
 *
 */

public class Individual implements Cloneable,Comparator<Individual>
{
    private int arraySize = 500;        // The default size of the geno-array
    int[] chromosome;                   // The chromosome: an array of gene 
    private int fitness = 0;            // The value of fitness 
    private boolean evaluated = false;  // The flag indicate if this individual is evaluated
    private Graph graph;                 // The graph definition

    public Individual(final int size, final Graph gbp){
        arraySize = size;
        randGene();
        graph = gbp;
        evalInd();
    }
    public Individual(final Individual ind){
        arraySize = ind.getArraySize();
        chromosome = new int[arraySize];
        setGenes(ind.getGenes());
        graph = ind.getGraph();
        fitness = ind.getFitness();
        evaluated = true;
    }

    /** Returning the size of the bit array **/
    public int getArraySize(){
        return arraySize;
    }
    /** The arraySize cannot be changed after initialization **/
    public void setArraySize(int newSize){
        System.out.println("The arraySize cannot be changed after initialization");
    }
    
    // chromosome: 
    /** Randomly generate an array of chromosome **/
    public void randGene(){
        chromosome = new int[arraySize];
        for(int i = 0; i < (arraySize/2); i++){
            chromosome[i] = 0;
        }
        for(int i = arraySize/2; i < arraySize; i++){
            chromosome[i] = 1;
        }
        Random rand = new Random();
        for(int i = 0; i < (arraySize-1); i++){
            int j = rand.nextInt((arraySize-i)) + i;
            int tmp = chromosome[i];
            chromosome[i] = chromosome[j];
            chromosome[j] = tmp;
        }
    }
    /** Retrieving chromosome **/
    public int[] getGenes(){
        return chromosome;
    }
    /** Replacing the the whole chromosome **/
    public void setGenes(final int[] oriGenes){
        for (int i = 0; i < arraySize; i++){
            this.chromosome[i] = oriGenes[i];
        }
        this.evaluated = false;
    }
    /** Modifying the chromosome **/
    public void setGene(final int position, final int gene){
        this.chromosome[position] = gene;
        this.evaluated = false;
    }

    // Fitness and evaluated 
    /** evaluate the individual **/
    public void evalInd(){
        this.fitness = calFitness();
        this.evaluated = true;
    }
    /** Returning evaluation **/
    public int getFitness(){
        if (this.evaluated){
            return this.fitness;
        }else{
            evalInd();
            return this.fitness;
        }
    }

    public void setgraph(final Graph gbp){
        System.out.println("The graph definition cannot be changed after initialization");
    }

    public Graph getGraph(){
        return this.graph;
    }

    /** Overriding the comparator between Individuals  **/
    public int compareTo(final Individual ind){
        int comp = 0;
        if (this.fitness!=ind.getFitness()){
            return (int)(this.fitness-ind.getFitness());
        } else {
            for (int i = 0; i < arraySize; i++){
                if (chromosome[i] != ind.getGenes()[i]){
                    comp = chromosome[i] - ind.getGenes()[i];
                    break;
                }
            }
            return comp;
        }
    }
//
// Implementing the Comparator
    public int compare(Individual ind1, Individual ind2){
        return (ind1.compareTo(ind2));
    }
    public boolean equals(Individual ind1, Individual ind2){
        return (ind1.compareTo(ind2)==0);
    }
//---------------------------------------------------------------------
    /** The fitness of this individual : cutSize **/
    private int calFitness(){
        int cutSize = 0;
        for (int i = 0; i < arraySize; i++){           
            if(chromosome[i]==0){                                          
	            int nodeID = graph.getVertex(i).label;         
	            int nNb = graph.getVertex(i).getNeighborCount();  
	            int[] nbs = graph.getVertex(i).getNeighborsVertices();   
	            
	            for (int j = 0; j < nNb; j++){
	                if(chromosome[(nodeID)]!= chromosome[(nbs[j])]){   
	                    cutSize++;                         
	                }
	            }
            }
        }
        return -1*cutSize;        
    }
}