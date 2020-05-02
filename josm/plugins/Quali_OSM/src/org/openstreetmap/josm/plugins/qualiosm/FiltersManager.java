
package org.openstreetmap.josm.plugins.qualiosm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.tools.ImageProcessor;


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
                
                
                // Aplicacao do filtro
                
             
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
