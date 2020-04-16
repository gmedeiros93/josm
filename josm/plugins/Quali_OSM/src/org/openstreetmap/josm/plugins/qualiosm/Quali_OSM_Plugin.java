package org.openstreetmap.josm.plugins.qualiosm;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.gui.help.HelpUtil;
import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Quali_OSM_Plugin extends Plugin {

    Adicionador_Tags adicionador_Tags;
    Classificador_Terreno classificador_Terreno;

  
      public Quali_OSM_Plugin(PluginInformation info) {
        super(info);
        // Inicializar plugin

        adicionador_Tags = new Adicionador_Tags();
        classificador_Terreno = new Classificador_Terreno();
      
         
        
        final JMenu loadTaskMenu = MainApplication.getMenu()
                .addMenu("QualiOSM", tr("QualiOSM"), KeyEvent.VK_K,
                MainApplication.getMenu().getDefaultMenuPos(), HelpUtil.ht("/Plugin/task")
        );
        loadTaskMenu.add(new JMenuItem(adicionador_Tags));
        loadTaskMenu.add(new JMenuItem(classificador_Terreno));
        
     
      
    }
        
}
