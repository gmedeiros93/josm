/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.qualiosm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.tools.ImageProcessor;

/**
 *
 * @author Gabriel
 */
public class FiltersManager implements ImageProcessor{
    
   
    
        public FilterActivate dialog;
    
    public FiltersManager(FilterActivate dialog) {
        this.dialog = dialog;
    }
   
  @Override
     public BufferedImage process(BufferedImage image) {

     

 while(true){
            try {
                // iterating through map of filters according to the order
                
                
                FilterTerrain filter = new FilterTerrain();
                
                
                // if next filter will return null
                // we should take an old example of the image
                BufferedImage oldImg = image;
                
                
                // applying filter to the current image
                
             
                image = filter.filter_terrain(image);
            
                
               
                
                
                if (image == null) {
                    image = oldImg;
                    
                    
                }
                
                return image;
            } catch (IOException ex) {
                Logger.getLogger(FiltersManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return image;
    }

     }  
    
}
