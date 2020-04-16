// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.kartverket;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.kartverket.actions.CheckDirectionAction;
import org.openstreetmap.josm.plugins.kartverket.actions.ReplaceWayAction;

public class KartverketPlugin extends Plugin {
    JMenuItem CheckDirection;
    JMenuItem mergeWays;
    /**
     * Will be invoked by JOSM to bootstrap the plugin
     *
     * @param info  information about the plugin and its local installation
     */
    public KartverketPlugin(PluginInformation info) {
        super(info);
        JMenu toolsMenu = MainApplication.getMenu().moreToolsMenu;
        CheckDirection = MainMenu.add(toolsMenu, new CheckDirectionAction());
        mergeWays = MainMenu.add(toolsMenu, new ReplaceWayAction());
    }

    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        boolean enabled = newFrame != null;
        enabled = false;
        CheckDirection.setEnabled(enabled);
    }
}
