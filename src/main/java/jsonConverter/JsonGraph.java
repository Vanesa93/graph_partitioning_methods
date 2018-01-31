package jsonConverter;

import java.util.ArrayList;

public class JsonGraph
{
   public ArrayList<VertexProperties> nodes;
   public ArrayList<EdgesProperties> edges;

   public JsonGraph(ArrayList<VertexProperties> nodes, ArrayList<EdgesProperties> edges)
   {
	   this.nodes= new ArrayList<VertexProperties>(nodes);
	   this.edges = new ArrayList<EdgesProperties>(edges);
   }

}