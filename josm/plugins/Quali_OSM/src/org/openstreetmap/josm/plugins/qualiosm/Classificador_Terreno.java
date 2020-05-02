// License: GPL. For details, see LICENSE file.
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

/**
 * This class toggles the wireframe rendering mode.
 * @since 2530
 */
public class Classificador_Terreno extends ToggleAction {
    
 protected Icon icon;


    /**
     * Constructs a new {@code WireframeToggleAction}.
     */
    public Classificador_Terreno() {
        super(tr("Enable/Disable Terrain Classifier"),
              null, /* no icon */
              tr("Enable/disable terrain classifier"),
              Shortcut.registerShortcut("menu:view:wireframe", tr("Toggle Wireframe view"), KeyEvent.VK_W, Shortcut.CTRL),
              false /* register toolbar */
        );
        putValue("toolbar", "wireframe");
        Action register = MainApplication.getToolbar().register(this);
        //setSelected(MapRendererFactory.getInstance().isWireframeMapRendererActive());
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
          //  System.out.println("Ativado!");
          
  
     
        } else {
         
            Layer  layer = (ImageryLayer) MainApplication.getLayerManager().getActiveLayer();
            
                    
            MainApplication.getLayerManager().removeLayer(layer);
            
        ImageryInfo info = new ImageryInfo(layer.getName(), "http://bar", "bing", null, null);
       
        Layer layer2 = ImageryLayer.create(info);
        
        MainApplication.getLayerManager().addLayer(layer2);
      MainApplication.getLayerManager().setActiveLayer(layer2);
        
    System.out.println("Desativado!");
          
        }
                
        notifySelectedState();
         
}

}




