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
    	String[] genOut = new String[4];    // For output
        //  Initialize statistics
        int max = -99999;                   // Max fitness of a generation
        int min = 99999;                    // Min fitness of a generation
        double mean = 0.0;                  // Mean fitness of a generation
    	 // maximum generation to evolve   	
    	maxGeneration = 100;          
        // number of Fiduccia Mattheyses locally
        int inFM = 0;                  
        // total number of Fiduccia Mattheyses
        int outFM = 0;                     
        int generation = 0;                 
        // Initialize population with Fiduccia Mattheyses
        Individual[] population = new Individual[populationSize];
        for (int i = 0; i <  populationSize; i++){
            population[i] = iterKLFM(new Individual(verticesCount,graph));
//            inFM = iterKLFM(population[i]);
//            outFM = outFM + inFM;
        }  
        // Start evolving
        while((generation <  maxGeneration) && // not reached the maximum generation
        	 (mean!=(double)max))              // Population are not converged 
        {
            Evolution evolution = new Evolution();
            // Random selection
            Individual[] parents = evolution.randSelection(population);
            // Generate off-spring with uniform crossover
            Individual offspring = new Individual(parents[0]);
            offspring.setGenes(evolution.uniformCrossover(parents[0], parents[1]));
            // Re-initiate offspring by Fiduccia Mattheyses
//            inFM = iterKLFM(offspring);
//            outFM = outFM + inFM;
            // Replace the worst case in the old population
            evolution.replaceWorst(offspring,population);
            offspring = iterKLFM(new Individual(offspring));
            // Increment generation
            generation++;
            genOut = (calStat(population));
            max = Integer.parseInt(genOut[1]);
            min = Integer.parseInt(genOut[2]);
            mean = Double.parseDouble(genOut[3]);
//            System.out.println(generation);
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
    public Individual iterKLFM(final Individual ind){
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
        System.out.println(countFM);
        return tempInd;
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
    
 // Calculate and output statistics
    /** Compute statistics to a string[]:
        0 - Output string
        1 - max fitness
        2 - min fitness
        3 - mean fitness                **/
    public String[] calStat(final Individual[] pop){
        // Initialze statistics
        int max = -99999;
        int min = 99999;
        int sum = 0;
        double mean = 0.0;
        String[] output = new String[4];

        // Calculateion
        for (int i = 0; i < pop.length; i++){
            int fit = pop[i].getFitness();
            sum += fit;                         // Sum-up fitness
            if(fit > max){                      // max: maximum fitness
                max = fit;
            }
            if(fit < min){                      // min:  minimum fitness
                min = fit;
            }
        }
        mean = (double)sum/(double)pop.length;
        output[0] = "\n";
        output[1] = Integer.toString(max);
        output[2] = Integer.toString(min);
        output[3] = Double.toString(mean);

        // Make output
        output[0] += "Fitness  Mean: "+mean+"   Max: "+max+"    Min: "+min +"\n";
        return output;
    }
}