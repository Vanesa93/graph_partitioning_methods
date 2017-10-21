package process_file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import graph.Graph;
import graph.Vertex;

/**
 * An implentation of process file class
 * 
 * @author Vanesa Georgieva
 *
 */

public class ProcessFile {

	private static BufferedReader br;
	public static Graph processFile(String filename, String type) {
		Graph graph = new Graph();
		// initialize some vertices and add them to the graph
		try {
		    String path = new File(filename).getAbsolutePath();
			br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			if (line != null) {
				String[] verticesNames = line.split(" ");
				Vertex[] vertices = new Vertex[verticesNames.length];
				for (int i = 0; i < vertices.length; i++) {
					vertices[i] = new Vertex(i);
					graph.addVertex(vertices[i], true);
				}
				int currentVertex = 0;
				while (line != null) {
					String[] tokens = line.split(" ");
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
