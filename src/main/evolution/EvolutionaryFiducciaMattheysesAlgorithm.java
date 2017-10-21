package evolution;

import java.util.*;

import graph.Graph;


/**
 * An implentation of the Fiduccia Mattheyses Algorithm used for evolutionary approach
 * 
 * @author Vanesa Georgieva
 *
 */

public class EvolutionaryFiducciaMattheysesAlgorithm{
    private Graph graph;                       
    private Individual oriInd, newInd;          
    private int[] unLockedVertices;                     
    private int[] gainValues;                     
    private LinkedList<Integer>[] bucket0, bucket1;        
    private int maxDegree;                        
    private int maxGain0,maxGain1;               
    private int balance;                          
    private LinkedList<Individual> candidates;   
	private int tmp;


    public EvolutionaryFiducciaMattheysesAlgorithm(final Graph gDef, final Individual ind){
        initKLFM(gDef,ind);
    }

    public void initKLFM(final Graph gDef, final Individual ind){
        oriInd = new Individual(ind);             
        newInd = new Individual(ind);             
        int[] oriGenes = copyArray(oriInd.getGenes()); 
        graph  = oriInd.getGraph();               

        int verticesSize = graph.getVertices().size();           
        maxDegree = findMaxDegree();           

        unLockedVertices = new int[verticesSize];              
        for (int i = 0; i < verticesSize; i++){          
            unLockedVertices[i] = 1;
        }
        
        gainValues = new int[verticesSize];                
        bucket0 = new LinkedList[(2*maxDegree+1)];  
        bucket1 = new LinkedList[(2*maxDegree+1)];  
        for(int i = 0; i < bucket0.length; i++){    
            bucket0[i] = new LinkedList<Integer>();
            bucket1[i] = new LinkedList<Integer>();
        }
        for (int i = 0; i < verticesSize; i++){       
            putBuckets((i),oriInd);
        }        
        findMaxGain(oriInd);
        balance = getBalance(oriGenes);
        candidates = new LinkedList<Individual>();
        candidates.add(new Individual(oriInd));
    }

    public Individual runKLFM(){
        Individual bestInd = new Individual(oriInd); 
        while(!allNodesLocked(newInd.getGenes())){            
            if(balance > 0){
                move1To0(newInd);
            } else if(balance < 0) {
                move0To1(newInd);
            } else {
                moveMaxGain(newInd);
            }
            balance = getBalance(newInd.getGenes());
            findMaxGain(newInd);
            if(balance==0){
                candidates.add(new Individual(newInd));
            }
        }
        int bestFitness = oriInd.getFitness();
        for(int i = 0; i < candidates.size(); i++){
            if(candidates.get(i).getFitness() > bestFitness){
                bestInd.setGenes(candidates.get(i).getGenes());
                bestFitness = bestInd.getFitness();
            }
        }
       
        return bestInd;
    }
        

    /** Move one node from partition 1 to partition 0 **/
    public void move1To0(final Individual ind){
        int[] tmpGenes = copyArray(ind.getGenes());
        int bIndex = maxDegree - maxGain1; // Get index: 0 if maxGain=maxDegree
        int nodeId = bucket1[bIndex].remove();    
        while(unLockedVertices[(nodeId)]==0){
            bucket1[bIndex].add(nodeId);        
            nodeId = bucket1[bIndex].remove();   
        }
        
        tmpGenes[nodeId] = 0; // Flip the partition
        ind.setGenes(tmpGenes);       
        unLockedVertices[nodeId] = 0;
        putBuckets(nodeId,ind);
        int[] nb = graph.getVertex(nodeId).getNeighborsVertices();
        for(int i = 0; i < graph.getVertex(nodeId).getNeighborCount();i++){
            removeFromBuckets(nb[i],ind);
            putBuckets(nb[i],ind);
        }
    }

    /** Move one node from partition 0 to partition 1 **/
    public void move0To1(final Individual ind){
        // Initialize working array
        int[] tmpGenes = copyArray(ind.getGenes());
        int bIndex = maxDegree - maxGain0;       
        int nodeId = bucket0[bIndex].remove();    
        while(unLockedVertices[(nodeId)]==0){
            bucket0[bIndex].add(nodeId);          
            nodeId = bucket0[bIndex].remove();    
        }        
        // Flip the partion
        tmpGenes[nodeId] = 1;                   
        ind.setGenes(tmpGenes);    
        unLockedVertices[nodeId] = 0;
        putBuckets(nodeId,ind);
        int[] nb = graph.getVertex(nodeId).getNeighborsVertices();
        for(int i = 0; i < graph.getVertex(nodeId).getNeighborCount();i++){
            removeFromBuckets(nb[i],ind);
            putBuckets(nb[i],ind);
        }
    }

    /** Move the node with maximum gain **/
    public void moveMaxGain(final Individual ind){
        if(maxGain1 > maxGain0){
            move1To0(ind);
        } else {
            move0To1(ind);
        }
    }

    public void putBuckets(final int nodeId, final Individual curInd){
        int i = nodeId;
        int[] tmpGenes = copyArray(curInd.getGenes());
        int gain = getGain(i, curInd);            // Gain = increased fitness
        gainValues[i] = gain;                     // Update the gain-values
        int bucketIndex = maxDegree - gain;       
        if(tmpGenes[i]==0){
            bucket0[bucketIndex].add(new Integer(nodeId));
        } else if(tmpGenes[i]==1){
            bucket1[bucketIndex].add(new Integer(nodeId));
        } else {
            System.out.println("    !!!     Unexpected partition, niether 0 nor 1    !!!!");
        }
    }

    public void removeFromBuckets(final int nodeId, final Individual curInd){
        int i = nodeId;
        int bucket = curInd.getGenes()[i];        // Which bucket
        int listIdx = maxDegree - gainValues[i];  // Which LinkedList
        int nodeIdx = 0;                          // Which element
        tmp = 0;
        
        switch(bucket){
        // The neighbor vertex is in bucket0
            case 0:                               
                nodeIdx = bucket0[listIdx].indexOf(nodeId);
                tmp = bucket0[listIdx].remove(nodeIdx);
                break;
        // The neighbor vertex is in bucket1
            case 1:                              
                nodeIdx = bucket1[listIdx].indexOf(nodeId);
                tmp = bucket1[listIdx].remove(nodeIdx);
                break;
            default:                           
                System.out.println("Unexpected partition,niether 0 nor 1");
                break;
        }
    }

	private int getGain( final int index, final Individual curInd ) {
        Individual tmpInd = new Individual(curInd);
        int[] tmpGenes = copyArray(tmpInd.getGenes());
        tmpGenes[index] = flipNode(tmpGenes[index]);          
        tmpInd.setGenes(tmpGenes);                            
        int gain = tmpInd.getFitness() - curInd.getFitness(); 
        return gain;
	}

    private int flipNode(int group){
        return (-1*group+1);
    }

    private void findMaxGain(final Individual ind){
        int verticesSize = graph.getVertices().size();         
        int[] tmpGenes = ind.getGenes();
        maxGain0 = -1*maxDegree;
        maxGain1 = -1*maxDegree;
        
        for(int i = 0; i < verticesSize; i++){
            switch(tmpGenes[i]){
                case 0:
                    if ((gainValues[i] > maxGain0)&&(unLockedVertices[i]==1)){
                        maxGain0 = gainValues[i];
                    } 
                    break;
                case 1:
                    if ((gainValues[i] > maxGain1)&&(unLockedVertices[i]==1)){
                        maxGain1 = gainValues[i];
                    }
                    break;
                default:
                    System.out.println("    !!!!    Wrong partition index    !!!!");
                    break;
            }
        }
    }

    /** 
        >0 : partition '1' have more nodes   
        <0 : partition '0' have more nodes   
        =0 : balanced                             
     **/
    private int getBalance(final int[] curPart){
        int bal = 0 - (curPart.length/2);         // Start from -1*(half of nodes)
        for(int i = 0; i < curPart.length; i++){
            bal+=curPart[i];                      
        }
        return bal;
    }

    private boolean allNodesLocked(final int[] curPart){
        int sum0 = 0;
        for(int i = 0; i < unLockedVertices.length; i++){
            sum0 += unLockedVertices[i];               
        }
        return ((sum0 == 0));
    }

	private int [] copyArray( int [] source ) {
		int [] result = new int[ source.length ];
		for( int i = 0; i < result.length; i++ )
			result[ i ] = source[ i ];
		return result;
	}

    private int findMaxDegree(){
        int maxDegree = 0;                            
        for (int i = 0; i < graph.getVertices().size(); i++){
            int nNeighbors = graph.getVertex(i).getNeighborCount();
            if(nNeighbors > maxDegree){
                maxDegree = nNeighbors;
            }
        }
        return maxDegree;
    }   
}