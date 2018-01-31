package jsonConverter;

import java.util.Random;

public class VertexProperties
{
   public String id;
   public String label;
   public String x;
   public String y;
   public String size;

   public VertexProperties(String id, String label, int maxCoordinate)
   {
      this.id = id;
      this.label = label;
      this.x = getRandomPosition(maxCoordinate).toString();
      this.y = getRandomPosition(maxCoordinate).toString();
      this.size = "3";
   }
   
   private Integer getRandomPosition(int maxCoordinate) {
	   Random rand = new Random(); 
	   return rand.nextInt(maxCoordinate); 
   }

}