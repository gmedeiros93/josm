/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.qualiosm;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 *
 * @author Gabriel
 */
public class FilterTerrain {
    
    
  public BufferedImage returnImage(BufferedImage workImage){
  return workImage;
  }
    
    
  public BufferedImage filter_terrain(BufferedImage workImage) throws IOException {
 
 BufferedReader br = new BufferedReader(new FileReader("classesy.txt"));

     // Store the classes color in a map.

     TreeMap<Integer,Color> classMap = new TreeMap<Integer, Color>();

     while(true)

       {

       String line = br.readLine(); 

       if (line == null) break;

       if (line.startsWith("#")) continue;

       StringTokenizer st = new StringTokenizer(line);

       if (st.countTokens() < 4) continue;

       int classId = Integer.parseInt(st.nextToken());

       int r = Integer.parseInt(st.nextToken());

       int g = Integer.parseInt(st.nextToken());

       int b = Integer.parseInt(st.nextToken());

       classMap.put(classId,new Color(r,g,b));

       }

     br.close();

     // Read the signatures from a file. 

     TreeMap<Integer,int[]> minMap = new TreeMap<>();

     TreeMap<Integer,int[]> maxMap = new TreeMap<>();

     br = new BufferedReader(new FileReader("signaturesy.txt"));

     while(true)

       {

       String line = br.readLine(); 

       if (line == null) break;

       if (line.startsWith("#")) continue;

       StringTokenizer st = new StringTokenizer(line);

       if (st.countTokens() < 7) continue;

       int classId = Integer.parseInt(st.nextToken());

       int[] min = new int[3]; int[] max = new int[3];

       min[0] = Integer.parseInt(st.nextToken());

       min[1] = Integer.parseInt(st.nextToken());

       min[2] = Integer.parseInt(st.nextToken());

       max[0] = Integer.parseInt(st.nextToken());

       max[1] = Integer.parseInt(st.nextToken());

       max[2] = Integer.parseInt(st.nextToken());

       minMap.put(classId,min);

       maxMap.put(classId,max);

       }

     br.close();

     // Create a color image to hold the results of the classification.

     int w = workImage.getWidth();  int h = workImage.getHeight();

     BufferedImage results = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

     // Do the classification, pixel by pixel, selecting which class they should be assigned to.

     for(int row=0;row<h;row++)

       for(int col=0;col<w;col++)

         {

         int rgb = workImage.getRGB(col,row);

         int r = (int)((rgb&0x00FF0000)>>>16); // Red level

         int g = (int)((rgb&0x0000FF00)>>>8);  // Green level

         int b = (int) (rgb&0x000000FF);       // Blue level

         // To which class should we assign this pixel?

         Color assignedClass = new Color(0,0,0); // unassigned.

         for(int key:minMap.keySet())

           {

           if (isBetween(r,g,b,minMap.get(key),maxMap.get(key))) 

             {

             assignedClass = classMap.get(key);            

             }

           }

         // With the color, paint the output image.

         results.setRGB(col,row,assignedClass.getRGB());

         }
     
     
return results;
        }
  
   private static boolean isBetween(int r,int g,int b,int[] min,int[] max)

     {

     return ((min[0] <= r) && (r <= max[0]) &&

             (min[1] <= g) && (g <= max[1]) &&

             (min[2] <= b) && (b <= max[2]));

     }
   
   
   
    
}
