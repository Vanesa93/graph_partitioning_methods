package jsonConverter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import entity.Edge;
import entity.Graph;
import entity.Vertex;

public class ObjectToJson {	
	
	public static void convertObjectToJson(String pathToSave, String filename, Graph graph) {
		try {
			int verticesCount = graph.getVertices().size();
			
			ObjectMapper mapperObj = new ObjectMapper();			
			ArrayList<VertexProperties> verticesList = new ArrayList<VertexProperties>();
			ArrayList<EdgesProperties> edgesList = new ArrayList<EdgesProperties>();
			
			for(Vertex v : graph.getVertices()){
				verticesList.add(new VertexProperties(v.getLabel().toString(), v.getLabel().toString(), verticesCount));
			 }
			
			Integer edgeId = 0;
			for(Edge e : graph.getEdges()){
				 edgesList.add(new EdgesProperties(edgeId.toString(), e.one.label.toString(), e.two.label.toString()));
				 edgeId++;
			 }
			
			StringWriter stringWriter = new StringWriter();
			JsonGraph jsonGraph = new JsonGraph(verticesList, edgesList);
			mapperObj.writeValue(stringWriter, jsonGraph);
//			System.out.println(stringWriter.toString());
			mapperObj.writeValue(new File(pathToSave + filename), jsonGraph);			
				
		} catch (JsonGenerationException ex) {
			ex.printStackTrace();
		} catch(JsonMappingException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
