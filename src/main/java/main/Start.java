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
		 String filename = "gC.txt";
		 Graph graph = ProcessFile.processFile(filename, matrixType, spaceSeparator);
		 runKernighanLin(graph);	 
		 runFiducciaMattheyses(graph);	
		 runNaiveApproach(graph);
		 runEvolution(graph);
	 }  
	
	 public static void runKernighanLin(Graph graph){
		 Runtime runtime = Runtime.getRuntime();
		 long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Used Memory before" + usedMemoryBefore);
		 long startTimeKL = System.currentTimeMillis();
		 new KernighanLinAlgorithm(graph);
		 long endTimeKL = System.currentTimeMillis();
		 double time_seconds = (endTimeKL-startTimeKL) / 1000.0;   // add the decimal
		 System.out.println(endTimeKL);

		 System.out.println("That took " + time_seconds + " seconds");
		 long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Memory increased:" + (usedMemoryAfter-usedMemoryBefore));
		 System.out.print("****************************************************************");
		 System.out.print(" \n");
	 }
	 
	 public static void runFiducciaMattheyses(Graph graph){
		 Runtime runtime = Runtime.getRuntime();
		 long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Used Memory before" + usedMemoryBefore);
		 long startTimeFM = System.currentTimeMillis();
		 new FiducciaMattheysesAlgorithm(graph);
		 long endTimeFM = System.currentTimeMillis();
		 double time_seconds = (endTimeFM-startTimeFM) / 1000.0;   // add the decimal
		 System.out.println("That took " + time_seconds + " seconds");
		 long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Memory increased:" + (usedMemoryAfter-usedMemoryBefore));
		 System.out.print("***************************************************************");
		 System.out.print(" \n");
	 }
	 
	 public static void runEvolution(Graph graph){
		 Runtime runtime = Runtime.getRuntime();
		 long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Used Memory before" + usedMemoryBefore);
		 long startTimeRE = System.currentTimeMillis();
		 new RunEvolution(graph);
		 long endTimeRE = System.currentTimeMillis();
		 System.out.print(" \n");
		 double time_seconds = (endTimeRE-startTimeRE) / 1000.0;   // add the decimal
		 System.out.println("That took " + time_seconds + " seconds");
		 long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Memory increased:" + (usedMemoryAfter-usedMemoryBefore));
		 System.out.print(" \n");
		 System.out.print("***************************************************************");
		 System.out.print(" \n");
	 }
	 
	 
	 public static void runNaiveApproach(Graph graph){
		 Runtime runtime = Runtime.getRuntime();
		 long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Used Memory before" + usedMemoryBefore);
		 long startTimeNA = System.currentTimeMillis();
		 new NaiveApproach(graph);
		 long endTimeNA = System.currentTimeMillis();
		 System.out.print(" \n");
		 double time_seconds = (endTimeNA-startTimeNA) / 1000.0;   // add the decimal
		 System.out.println("That took " + time_seconds + " seconds");
		 long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Memory increased:" + (usedMemoryAfter-usedMemoryBefore));
		 System.out.print(" \n");
		 System.out.print("***************************************************************");
		 System.out.print(" \n");
	 }
}
