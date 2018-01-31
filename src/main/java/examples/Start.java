package examples;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import classic_algorithms.FiducciaMattheysesAlgorithm;
import classic_algorithms.KernighanLinAlgorithm;
import entity.Graph;
import evolutionary_approach.EvolutionaryApproach;
import naive_approach.NaiveApproach;
import helpers.ProcessFile;
import jsonConverter.ObjectToJson;

public class Start {

	static String matrixType = "matrix";
	static String rowType = "row";
	static String spaceSeparator = "space";
	static String commaSeparator = "comma";
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException{
		 String filename = "graph.txt";
		 Graph graph = ProcessFile.processFile(filename, matrixType, commaSeparator, "");
//		 ObjectToJson.convertObjectToJson("D:\\saved\\", filename,  graph);
		 runKernighanLin(graph);	 
		 runFiducciaMattheyses(graph);	
		 runNaiveApproach(graph);
		 runEvolution(graph);
		 graph = null;
	 }  
	
	 public static void runKernighanLin(Graph graph){
		 System.out.println("Kernighan Lin");
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
	 
	 public static void runEvolution(Graph graph) {
		 Runtime runtime = Runtime.getRuntime();
		 long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
		 System.out.println("Used Memory before" + usedMemoryBefore);
		 long startTimeRE = System.currentTimeMillis();
		 new EvolutionaryApproach(graph);
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
		 try {
			new NaiveApproach(graph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
