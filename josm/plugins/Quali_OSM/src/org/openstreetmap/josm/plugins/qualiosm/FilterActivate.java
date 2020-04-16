/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.qualiosm;






import org.openstreetmap.josm.gui.layer.ImageryLayer;
import org.openstreetmap.josm.gui.layer.Layer;



/**
 * This filters is responsible for creating filter's dialog where user can
 * choose and add new filter at this dialog.
 *
 * @author Gabriel Medeiros
 */
public class FilterActivate {

  
    
    private Layer layer;
    private FiltersManager filtersManager;
   
   

    public FilterActivate(ImageryLayer layer) {
        
        this.layer = layer;
        this.filtersManager = new FiltersManager(this);
        layer.addImageProcessor(filtersManager);
    }

    FilterActivate() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}