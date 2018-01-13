package evolutionary_approach;

import java.util.*;

import entity.Graph;
import entity.Individual;


/**
 * An implentation of the Fiduccia Mattheyses Algorithm used for evolutionary approach
 * 
 * @author Vanesa Georgieva
 *
 */

public interface IEvolutionaryFiducciaMattheysesAlgorithm {
    void initKLFM(final Graph gDef, final Individual ind);
    Individual runKLFM();
    void move1To0(final Individual ind);
    void move0To1(final Individual ind);
    void moveMaxGain(final Individual ind);
    void putBuckets(final int nodeId, final Individual curInd);
    void removeFromBuckets(final int nodeId, final Individual curInd);
    int getGain( final int index, final Individual curInd );
    int flipNode(int group);
    void findMaxGain(final Individual ind);
    int getBalance(final int[] curPart);
    boolean allNodesLocked(final int[] curPart);
    int [] copyArray( int [] source );
    int findMaxDegree(); 
}