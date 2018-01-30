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

public interface IEvolution {

    Individual[] randomSelection(final Individual[] pop);
    int[] uniformCrossover(final Individual ind1, final Individual ind2);
    void replaceWorst(final Individual ind, final Individual[] pop);
    int [] copyArray( int [] source );
    void checkHammingDistance(final Individual ind1, final Individual ind2);
    int [] flipArray( int [] source );
    void randPermute( int[] a );
}
