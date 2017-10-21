package main;

import algorithms.FiducciaMattheysesAlgorithm;
import algorithms.KernighanLinAlgorithm;
import graph.Graph;
import naive_approach.NaiveApproach;
import process_file.ProcessFile;
import evolution.RunEvolution;

public class Start {

	static String matrixType = "matrix";
	static String rowType = "row";
	
	public static void main(String[] args){
		 String filename = "graphE.txt";
		 Graph graph = ProcessFile.processFile(filename, matrixType);
		 
		 new KernighanLinAlgorithm(graph);
		 System.out.print(" \n");
		 
		 new FiducciaMattheysesAlgorithm(graph);
		 System.out.print(" \n");
		 
		 new RunEvolution(graph);
		 System.out.print(" \n");
		 
		 new NaiveApproach(graph);
		 System.out.print(" \n");
	 }  
}
