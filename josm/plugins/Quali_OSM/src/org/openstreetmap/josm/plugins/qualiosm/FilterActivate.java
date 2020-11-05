/***************************************************
* File: FilterActivate.java
* Author: Gabriel Franklin Braz de Medeiros
* Programa de Pos-Graduacao em Informatica
* University of Brasilia
* Professor Maristela Terto de Holanda
*****************************************************/

package org.openstreetmap.josm.plugins.qualiosm;

import org.openstreetmap.josm.gui.layer.ImageryLayer;
import org.openstreetmap.josm.gui.layer.Layer;

/***************************************************
 * Class FilterActivate is responsible for activating
 * the filter implemented by the terrain classifier.
 ***************************************************/

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
