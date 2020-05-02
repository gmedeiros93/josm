package org.openstreetmap.josm.plugins.qualiosm;

import org.openstreetmap.josm.gui.layer.ImageryLayer;
import org.openstreetmap.josm.gui.layer.Layer;

/**
 * Ativacao do Filtro do Classificador de Terrenos
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
