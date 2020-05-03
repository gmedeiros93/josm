/***************************************************
* File: FiltersManager.java
* Author: Gabriel Franklin Braz de Medeiros
* Programa de Pos-Graduacao em Informatica
* University of Brasilia
* Professor Maristela Terto de Holanda
*****************************************************/
package org.openstreetmap.josm.plugins.qualiosm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.tools.ImageProcessor;

/**********************************************************
Class FiltersManager is responsible for calling the methods 
that perform image processing used by the filter.
**********************************************************/

public class FiltersManager implements ImageProcessor{
    
public FilterActivate dialog;
    
public FiltersManager(FilterActivate dialog) {
        this.dialog = dialog;
    }
   
  @Override
     public BufferedImage process(BufferedImage image) {

while(true){
            try {  
                FilterTerrain filter = new FilterTerrain();
                BufferedImage oldImg = image;
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
