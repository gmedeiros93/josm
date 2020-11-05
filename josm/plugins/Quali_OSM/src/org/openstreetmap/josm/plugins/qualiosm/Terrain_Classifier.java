/**************************************************
* File: Terrain_Classifier.java
* Author: Gabriel Franklin Braz de Medeiros
* Programa de Pos-Graduacao em Informatica
* University of Brasilia
* Professor Maristela Terto de Holanda
*****************************************************/

package org.openstreetmap.josm.plugins.qualiosm;

import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.Icon;
import org.openstreetmap.josm.tools.Shortcut;
import org.openstreetmap.josm.actions.ToggleAction;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.data.imagery.ImageryInfo;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.ImageryLayer;

/************************************************************************************
 * Class Terrain_Classifier is derived from ToggleAction, which means that the button
 * has two different states: enabled and disabled.
 * When state is enabled, the terrain classifier should be applyed.
 ************************************************************************************/
public class Terrain_Classifier extends ToggleAction {
    
 protected Icon icon;

    public Terrain_Classifier() {
        super(tr("Classificador de Paralelepípedo"),
              "terrain.png", /* no icon */
              tr("Enable/disable terrain classifier"),
              Shortcut.registerShortcut("menu:view:wireframe", tr("Toggle Wireframe view"), KeyEvent.VK_W, Shortcut.CTRL),
              false /* register toolbar */
        );
        putValue("toolbar", "wireframe");
        Action register = MainApplication.getToolbar().register(this);
      
        notifySelectedState();
    }

    @Override
   protected void updateEnabledState() {
        if (!(MainApplication.getLayerManager().getActiveLayer() instanceof ImageryLayer)) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
       
        toggleSelectedState(e);
     
        if (isSelected()) {
                  
            FilterActivate filterActivate = new FilterActivate((ImageryLayer) MainApplication.getLayerManager().getActiveLayer());
           
            MainApplication.getLayerManager().getActiveLayer().setFilterStateChanged();
     
          
  
     
        } else {
         
            Layer  layer = (ImageryLayer) MainApplication.getLayerManager().getActiveLayer();
            
                    
            MainApplication.getLayerManager().removeLayer(layer);
            
        ImageryInfo info = new ImageryInfo(layer.getName(), "http://bar", "bing", null, null);
       
        Layer layer2 = ImageryLayer.create(info);
        
        MainApplication.getLayerManager().addLayer(layer2);
      MainApplication.getLayerManager().setActiveLayer(layer2);
        
  
          
        }
                
        notifySelectedState();
         
}

}




