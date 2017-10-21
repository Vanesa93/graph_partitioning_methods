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
	static String spaceSeparator = "space";
	static String commaSeparator = "comma";
	
	public static void main(String[] args){
		 String filename = "loooong.txt";
		 Graph graph = ProcessFile.processFile(filename, rowType, spaceSeparator);
		 
//		 long startTimeKL = System.currentTimeMillis();
//		 new KernighanLinAlgorithm(graph);
//		 long endTimeKL = System.currentTimeMillis();
//		 System.out.println("That took " + (endTimeKL - startTimeKL) + " milliseconds");
//		 System.out.print(" \n");
//		 
//		 
//		 long startTimeFM = System.currentTimeMillis();
//		 new FiducciaMattheysesAlgorithm(graph);
//		 long endTimeFM = System.currentTimeMillis();
//		 System.out.println("That took " + (endTimeFM - startTimeFM) + " milliseconds");
//		 System.out.print(" \n");
		 
		 long startTimeRE = System.currentTimeMillis();
		 new RunEvolution(graph);
		 long endTimeRE = System.currentTimeMillis();
		 System.out.print(" \n");
		 System.out.println("That took " + (endTimeRE - startTimeRE) + " milliseconds");
		 System.out.print(" \n");
		 
//		 long startTimeNA = System.currentTimeMillis();
//		 new NaiveApproach(graph);
//		 long endTimeNA = System.currentTimeMillis();
//		 System.out.print(" \n");
//		 System.out.println("That took " + (endTimeNA - startTimeNA) + " milliseconds");
//		 System.out.print(" \n");
	 }  
}
