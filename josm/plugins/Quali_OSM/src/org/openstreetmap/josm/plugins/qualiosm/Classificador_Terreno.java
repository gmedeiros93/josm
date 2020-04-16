package org.openstreetmap.josm.plugins.qualiosm;


import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;
import org.openstreetmap.josm.gui.layer.ImageryLayer;



/**
 *
 * @author Gabriel
 */
public class Classificador_Terreno extends JosmAction  {
    
    public boolean enableFilter = true;
    
    
    public Classificador_Terreno() {
    super(tr("Apply Terrain Classifier"), new ImageProvider("terrain.png"), tr("Apply Terrain Classifier"),
                Shortcut.registerShortcut("Apply Terrain Classifier", tr("Apply Terrain Classifier"),
                        KeyEvent.VK_A, Shortcut.CTRL_SHIFT), false, "ClassificaTerreno",
                        true);
   
    }
    
   
    @Override
    public void actionPerformed(ActionEvent e) {
   
 
    FilterActivate dialog = new FilterActivate((ImageryLayer) MainApplication.getLayerManager().getActiveLayer());
            MainApplication.getLayerManager().getActiveLayer().setFilterStateChanged();
          
         
            
  
    }
  
     
    @Override
    protected void updateEnabledState() {
        if (!(MainApplication.getLayerManager().getActiveLayer() instanceof ImageryLayer)) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }
    
           


   

     
}
