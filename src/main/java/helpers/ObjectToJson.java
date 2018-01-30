package helpers;

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
	
	
public static String convertObjectToJson(String pathToSave, Graph graph) throws JsonGenerationException, JsonMappingException, IOException{
		
		ObjectMapper mapperObj = new ObjectMapper();
		
		
		ArrayList<VertexProperties> verticesList = new ArrayList<VertexProperties>();
		ArrayList<EdgesProperties> edgesList = new ArrayList<EdgesProperties>();

		for(Vertex v : graph.getVertices()){
			verticesList.add(new VertexProperties(v.getLabel().toString(), v.getLabel().toString()));
		 }
		
		Integer i = 0;
		for(Edge e : graph.getEdges()){
			 edgesList.add(new EdgesProperties(i.toString(), e.one.label.toString(), e.two.label.toString()));
			 i++;
		 }
		
		StringWriter stringWriter = new StringWriter();
		JsonGraph jsonGraph = new JsonGraph(verticesList, edgesList);
		mapperObj.writeValue(stringWriter, jsonGraph);
		System.out.println(stringWriter.toString());
		mapperObj.writeValue(new File(pathToSave), jsonGraph);

			return pathToSave;
		
//		String jsonStr = "";
//		try {
//			// get Employee object as a json string
//			jsonStr = mapperObj.writeValueAsString(jsonGraph);
//			System.out.println(jsonStr);
//			mapperObj.writeValue(new File(pathToSave), jsonGraph);
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return jsonStr;
	}

public static class JsonGraph
{
   public ArrayList<VertexProperties> nodes;
   public ArrayList<EdgesProperties> edges;

   public JsonGraph(ArrayList<VertexProperties> nodes, ArrayList<EdgesProperties> edges)
   {
	   this.nodes= new ArrayList<VertexProperties>(nodes);
	   this.edges = new ArrayList<EdgesProperties>(edges);
   }

}

public static class VertexProperties
{
   public String id;
   public String label;
   public String x;
   public String y;
   public String size;

   public VertexProperties(String id, String label)
   {
      this.id = id;
      this.label = label;
      this.x = getRandomPosition().toString();
      this.y = getRandomPosition().toString();
      this.size = "3";
   }
   
   private Integer getRandomPosition() {
	   Random rand = new Random(); 
	   return rand.nextInt(50); 
   }

}

public static class EdgesProperties
{
   public String id;
   public String source;
   public String target;
   
   public EdgesProperties(String id, String source, String target)
   {
      this.id = id;
      this.source = source;
      this.target = target;
   }

}
	
}
