package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import entity.Graph;
import entity.Vertex;

/**
 * An implentation of process file class
 * 
 * @author Vanesa Georgieva
 *
 */

public class ProcessFile {

	private static BufferedReader br;
	static String matrixType = "matrix";
	static String rowType = "row";
	static String spaceSeparator = "space";
	static String commaSeparator = "comma";
	
	public static Graph processFile(String filename, String type, String separator,  String path) {
		Graph graph = new Graph();
		// initialize some vertices and add them to the graph
		try {
			// Start reading file
			path = path == "" ? new File(filename).getAbsolutePath() : path;
			br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			String[] tokens = null;
			if(type == matrixType) {
				String[] verticesNames = null;
				if (line != null) {
					if(separator == spaceSeparator) verticesNames = line.split(" ");
					else if(separator == commaSeparator){
						String currentline = line.replaceAll("\\s+","");
						verticesNames = currentline.split(",");
					}
					Vertex[] vertices = new Vertex[verticesNames.length];
					for (int i = 0; i < vertices.length; i++) {
						vertices[i] = new Vertex(i,i);
						graph.addVertex(vertices[i], true);
					}
					int currentVertex = 0;
					while (line != null) {
						if(separator == spaceSeparator){
							tokens = line.split(" ");
						} else if(separator == commaSeparator){
							String currentline = line.replaceAll("\\s+","");
							tokens = currentline.split(",");
						}
						for (int i = 0; i < tokens.length; i++) {
							int currentToken = Integer.parseInt(tokens[i]);
							if (currentToken == 1) {
								graph.addEdge(vertices[currentVertex], vertices[i],currentToken);
							}
						}
						currentVertex++;
						line = br.readLine();
					}
				}
				br.close();
			} else if(type == rowType) {
				int y = 0;
				int label = 0;
				while(line!=null) {
					String currentline = line.replaceAll("a\\s","");
					tokens = currentline.split(" ");
					int potentialVertexnName1 = Integer.parseInt(tokens[y]);
					int potentialVertexName2 = Integer.parseInt(tokens[y+1]);
					int potentialEdgeWeight = Integer.parseInt(tokens[y+1]);
					Vertex vertex1 = graph.getVertexByName(potentialVertexnName1);
					if(vertex1 == null) {
						vertex1 = new Vertex(label, potentialVertexnName1);
						graph.addVertex(vertex1, false);
						label++;
					}
					Vertex vertex2 = graph.getVertexByName(potentialVertexName2);
					if(vertex2 == null) {
						vertex2 = new Vertex(label, potentialVertexName2);
						graph.addVertex(vertex2, false);
						label++;
					}
					
					if(!graph.isConnected(vertex1, vertex2) && !graph.isConnected(vertex2, vertex1)) {
						graph.addEdge(vertex1, vertex2, potentialEdgeWeight);
					}
					line = br.readLine();							
				}
				br.close();
			}   
		} catch (FileNotFoundException ex) {

			ex.printStackTrace();

		} catch (IOException ex) {

			ex.printStackTrace();

		} catch (Exception e) {

			System.out.println("Error: " + e);
		}
		return graph;
	}
	
//	// will be used to create file
//	private static void createFile(String file, ArrayList<String> arrData)
//            throws IOException {
//        FileWriter writer = new FileWriter(file + ".txt");
//        int size = arrData.size();
//        for (int i=0;i<size;i++) {
//            String str = arrData.get(i).toString();
//            writer.write(str);
//            if(i < size-1)//This prevent creating a blank like at the end of the file**
//                writer.write("\n");
//        }
//        writer.close();
//    }
}
