package evolutionary_approach;

import java.util.*;

import entity.Graph;
import entity.Individual;

/**
 *   An implentation of the selection, replacement and recombination functions 
 *   needed for evolution
 *  
 * @author Vanesa Georgieva
 *
 */

public class EvolutionService implements IEvolution{

	// Selection
    /** Randomly Select 2 parents from the given population **/
    public Individual[] randomSelection(final Individual[] pop){
        // Generate 2 random index numbers from given population
        Random rand = new Random();
        int pSize = pop.length;
        int indIndex1 = rand.nextInt(pSize);
        int indIndex2 = rand.nextInt(pSize);
        while(indIndex2 == indIndex1){
            indIndex2 = rand.nextInt(pSize);
        }       
        // Copy selected individuals as parents
        Individual[] parents = new Individual[2];
        parents[0] = new Individual(pop[indIndex1]);
        parents[1] = new Individual(pop[indIndex2]);
        return parents;
    }

    // Recombination
   /** Uniformcrossover **/
    public int[] uniformCrossover(final Individual ind1, final Individual ind2)
    {
        int genoSize = ind1.getArraySize();     // Size of the genes
        int[] newGenes = new int[genoSize];     // new genes to return
        LinkedList<Integer> mismatch = new LinkedList<Integer>();

        // Check hamming distance: For GBP, 0011 and 1100 represent the same partitioning
        checkHammingDistance(ind1, ind2);
        
        // Check exception
        if(genoSize!=ind2.getArraySize()){
            System.out.println("Parents with different length, return a random Individual");
        } else {    
            // Recording mismatched positions
            for (int i = 0; i < genoSize; i++){
                if(ind1.getGenes()[i]==ind2.getGenes()[i]){
                    newGenes[i] = ind1.getGenes()[i];
                } else {
                    newGenes[i] = -1;
                    mismatch.add(i);
                }
            }
            // Randomly assign half 0's and half 1's to mismatched positions
            int[] m = new int[mismatch.size()];
            for (int i = 0; i < (mismatch.size()); i++){
                m[i] = mismatch.get(i);
            }
            randPermute(m);
            for (int i = 0; i < (m.length/2); i++){
                newGenes[m[i]] = 0;
            }
            for (int i = (m.length/2); i < m.length; i++){
                 newGenes[m[i]] = 1;
            }
        }
        return newGenes;
    }

    // Replacement
   /** Replace the worst individual **/
    public void replaceWorst(final Individual ind, final Individual[] pop)
    {
        Arrays.sort(pop, pop[0]);
        pop[0] = new Individual(ind);
    }

    /** Copy an integer array by values **/ 
	public int [] copyArray( int [] source ) {
		int [] result = new int[ source.length ];
		for( int i = 0; i < result.length; i++ )
			result[ i ] = source[ i ];
		return result;
	}
    
    /** check hamming distance **/ 
	public void checkHammingDistance(final Individual ind1, final Individual ind2) {
		int hammingDistance = 0;
        int[] a1 = copyArray(ind1.getGenes());
        int[] a2 = copyArray(ind2.getGenes());
		// Count difference
        for( int i = 0; i < a1.length; i++ ){
            if (a1[i] != a2[i]){
                hammingDistance ++;
            }
        }
		// If difference > (number of vertices/2), flip one
        if(hammingDistance>(a1.length/2)){
            int[] newA = flipArray(a2);
            ind2.setGenes(newA);
        }
	}

    /** Reverse the values of a bit string **/ 
	public int [] flipArray( int [] source ) {
		int [] result = new int[ source.length ];
		for( int i = 0; i < result.length; i++ )
			result[ i ] = (-1 * source[ i ]) + 1;
		return result;
	}

    /** Perform random permutation on an integer array **/ 
	public void randPermute( int[] a ) {
        Random rand = new Random();
        for(int i = 0; i < (a.length-1); i++){
            int j = rand.nextInt((a.length-i)) + i;
            // Swap a[i] and a[j]
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }
}
