package jsonConverter;

import java.util.Random;

public class VertexProperties
{
   public String id;
   public String label;
   public Integer x;
   public Integer y;
   public String size;

   public VertexProperties(String id, String label, Integer x, Integer y)
   {
      this.id = id;
      this.label = label;
      this.x = x;
      this.y = y;
      this.size = "3";
   }
  
}