package evolutionary_approach;

import entity.Graph;
import entity.Individual;
import entity.VertexGroup;


/**
 *  This is the main program performing
 *  the Graph Partitioning Problem using Fiduccia Mattheyses Algorithm with 
 *  evolutionary approach.
 *  
 * @author Vanesa Georgieva
 *
 */

public interface IEvolutionaryApproach{
    
    void processGraph(Graph g);
    Individual iterKLFM(final Individual ind);
    int sumArray(int[] a);
    void showArray(int[] a);              
    String[] calStat(final Individual[] pop);
    
}