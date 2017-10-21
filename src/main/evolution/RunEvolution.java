package evolution;

import graph.Graph;
import graph.VertexGroup;


/**
 *  This is the main program performing
 *  the Graph Partitioning Problem using Fiduccia Mattheyses Algorithm with 
 *  evolutionary approach.
 *  
 * @author Vanesa Georgieva
 *
 */

public class RunEvolution{
    private Graph graph;
	private int populationSize;
	private int verticesCount;
	private int optimumFitness;
	private int maxGeneration;
	public VertexGroup partition1;
	public VertexGroup partition2;
	
	public RunEvolution(Graph g){
        processGraph(g);
    	System.out.print("Evolutionary Fiduccia Mattheysis algorithm");
        System.out.print(" \n");
        System.out.print("CLuster 1");
        System.out.print(" \n");
        System.out.print(partition1);
        System.out.print(" \n");
        System.out.print("CLuster 2");
        System.out.print(" \n");
        System.out.print(partition2);
    }

    /**  Methods to run EC with KLFM-initialization **/ 
    public void processGraph(Graph g){
    	graph = g;
    	verticesCount = graph.getVertices().size();        
    	// size of population
    	populationSize = graph.getVertices().size()/2 ;        
    	 // fitness of known optimum
    	optimumFitness = -1;         
    	 // maximum generation to evolve   	
    	maxGeneration = 100;        
    	 // Max fitness of a generation
        int max = -99999;              
        // Mean fitness of a generation
        double mean = 0.0;        
        // number of Fiduccia Mattheyses locally
        int inFM = 0;                  
        // total number of Fiduccia Mattheyses
        int outFM = 0;                     
        int generation = 0;                 
        // number of optimum found
        int optCount = 0;                  

        // Initialize population with Fiduccia Mattheyses
        Individual[] population = new Individual[populationSize];
        for (int i = 0; i <  populationSize; i++){
            population[i] = new Individual(verticesCount,graph);
            inFM = iterKLFM(population[i]);
            outFM = outFM + inFM;
            // Add counter if optimum found
            if(population[i].getFitness() >=  optimumFitness){
            	// probability of optimum found
                optCount++;            
            }
        }        
        // Start evolving
        while((generation <  maxGeneration)&&       // not reached the maximum generation
              (max!= optimumFitness)&&               // not found best fitness
              (mean!=(double)max))                //  not converged population
        {
            Evolution evolution = new Evolution();
            // Random selection
            Individual[] parents = evolution.randSelection(population);
            // Generate off-spring with uniform crossover
            Individual offspring = new Individual(parents[0]);
            offspring.setGenes(evolution.uniformCrossover(parents[0], parents[1]));
            // Re-initiate offspring by Fiduccia Mattheyses
            inFM = iterKLFM(offspring);
            outFM = outFM + inFM;
            // Add counter if optimum found
            if(offspring.getFitness() >=  optimumFitness){
            	// the probability of optimum found
                optCount++;           
            }
            // Replace the worst case in the old population
            evolution.replaceWorst(offspring,population);
            // Increment generation
            generation++;
        }
        partition1 = new VertexGroup();
        partition2 = new VertexGroup();

        // create first partition
       	for(int y = 0; y<population[populationSize-1].chromosome.length; y++){
       		if(population[populationSize-1].chromosome[y] == 1) partition1.add(graph.getVertex(y));
       	}

        // create second partition
       	for(int y = 0; y<population[populationSize-1].chromosome.length; y++){
        	if(population[populationSize-1].chromosome[y] == 0)  partition2.add(graph.getVertex(y));
        }
       	if(partition1.contains(verticesCount)){
       		partition1.remove(verticesCount);
   		} else if(partition2.contains(verticesCount)){
   			partition2.remove(verticesCount);
       	}
    }

    /** Iterative Fiduccia Mattheyses until no improvement **/
    public int iterKLFM(final Individual ind){
        int countFM = 0;
        Individual tempInd = new Individual(ind);
        EvolutionaryFiducciaMattheysesAlgorithm fm = new EvolutionaryFiducciaMattheysesAlgorithm(graph,ind);
        tempInd = fm.runKLFM();
        while(tempInd.getFitness() > ind.getFitness()){
            ind.setGenes(tempInd.getGenes());
            fm.initKLFM(graph,ind);
            tempInd = fm.runKLFM();
            countFM++;
        }
        return countFM;
    }
  
    public int sumArray(int[] a){
        int sum = 0;
        for(int i = 0; i < a.length; i++){
            sum += a[i];
        }
        return sum;
    }

    public void showArray(int[] a){
        String output = "Array Values: \n";
        for(int i = 0; i < a.length; i++){
           output = output + " " + a[i];
           if((i%25)==24) output = output + "\n";
        }
        System.out.println(output);
    }
}