/***************************************************
* File: Quali_OSM_Plugin.java
* Author: Gabriel Franklin Braz de Medeiros
* Programa de Pos-Graduacao em Informatica
* Universidade de Brasilia
* Professor Maristela Terto de Holanda
*****************************************************/

package org.openstreetmap.josm.plugins.qualiosm;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.gui.help.HelpUtil;
import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


/******************************************************************
* Class Quali_OSM_Plugin is derived from Plugin, base class helper 
for all plugins in JOSM.
******************************************************************/

public class Quali_OSM_Plugin extends Plugin {

    Tag_Adder adicionador_Tags;
    Tags_Correios postcode_correios;
    Tags_Nominatim postcode_nominatim;
    Tags_Cepaberto postcode_cepaberto;
    Terrain_Classifier classificador_Terreno;
    Limpa_Tags limpa_tags;
    
   

  
      public Quali_OSM_Plugin(PluginInformation info) {
        super(info);
        // Initialize plugin

        adicionador_Tags = new Tag_Adder();
       classificador_Terreno = new Terrain_Classifier();
        postcode_correios = new Tags_Correios();
        postcode_nominatim = new Tags_Nominatim();
        postcode_cepaberto = new Tags_Cepaberto();
        limpa_tags = new Limpa_Tags();
      
   final JMenu loadTaskMenu = MainApplication.getMenu()
                .addMenu("QualiOSM", tr("QualiOSM"), KeyEvent.VK_K,
                MainApplication.getMenu().getDefaultMenuPos(), HelpUtil.ht("/Plugin/task")
        );
        loadTaskMenu.add(new JMenuItem(adicionador_Tags));
         loadTaskMenu.add(new JMenuItem(postcode_nominatim));
        loadTaskMenu.add(new JMenuItem(postcode_correios));
        loadTaskMenu.add(new JMenuItem(postcode_cepaberto));
        loadTaskMenu.add(new JMenuItem(limpa_tags));
        //loadTaskMenu.add(new JMenuItem(classificador_Terreno));
        
      
         
    }
        
}
