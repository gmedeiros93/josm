// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.kartverket.actions;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.plugins.utilsplugin2.replacegeometry.ReplaceGeometryCommand;
import org.openstreetmap.josm.plugins.utilsplugin2.replacegeometry.ReplaceGeometryException;
import org.openstreetmap.josm.plugins.utilsplugin2.replacegeometry.ReplaceGeometryUtils;
import org.openstreetmap.josm.tools.Shortcut;

/**
 * Replaces already existing object (id&gt;0) with a new object (id&lt;0).
 *
 * @author Torstein Bø
 */
public class ReplaceWayAction extends JosmAction {
    private static final String TITLE = tr("Replace way");

    public ReplaceWayAction() {
        super(TITLE, null, tr("Replace way of selected way with a new way"),
                Shortcut.registerShortcut("tools:replacecoastline", tr("Tool: {0}", tr("Replace Geometry")), KeyEvent.VK_S, Shortcut.CTRL_SHIFT),
                true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getLayerManager().getEditDataSet() == null) {
            return;
        }

        // There must be two ways selected: one with id > 0 and one new.
        List<OsmPrimitive> selection = new ArrayList<>(getLayerManager().getEditDataSet().getSelected());
        if (selection.size() != 2 || !(selection.get(0) instanceof Way && selection.get(1) instanceof Way)) {
            new Notification(
                    tr("This tool replaces coastline of one way with another, and so requires exactly two coatline ways to be selected.")
                    ).setIcon(JOptionPane.WARNING_MESSAGE).show();
            return;
        }

        Way firstObject = (Way) selection.get(0);
        Way secondObject = (Way) selection.get(1);
        Map<String, String> keys;
        if (firstObject.getId() <= 0) {
            keys = firstObject.getKeys();
        } else {
            keys = secondObject.getKeys();
        }
        String source = null;
        String sourceDate = null;
        if (keys.containsKey("source")) {
            source = keys.get("source");
        }
        if (keys.containsKey("source:date")) {
            sourceDate = keys.get("source:date");
        }
        setSourceAndFixme(firstObject, source, sourceDate);
        setSourceAndFixme(secondObject, source, sourceDate);

        try {
            firstObject.getKeys();

            ReplaceGeometryCommand replaceCommand =
                    ReplaceGeometryUtils.buildReplaceWayWithNewCommand(Arrays.asList(firstObject, secondObject));

            // action was canceled
            if (replaceCommand == null)
                return;

            UndoRedoHandler.getInstance().add(replaceCommand);
        } catch (IllegalArgumentException ex) {
            new Notification(
                    ex.getMessage()
                    ).setIcon(JOptionPane.WARNING_MESSAGE).show();
        } catch (ReplaceGeometryException ex) {
            new Notification(
                    ex.getMessage()
                    ).setIcon(JOptionPane.WARNING_MESSAGE).show();
        }
    }

    private void setSourceAndFixme(Way way, String source, String sourceDate) {
        Map<String, String> keys = way.getKeys();
        if (source != null)
            keys.put("source", source);
        if (sourceDate != null)
            keys.put("source:date", sourceDate);
        if (keys.containsKey("FIXME") && keys.get("FIXME") == "Merge") {
            keys.remove("FIXME");
        }
        way.setKeys(keys);
    }

    @Override
    protected void updateEnabledState() {
        if (getLayerManager().getEditDataSet() == null) {
            setEnabled(false);
        } else
            updateEnabledState(getLayerManager().getEditDataSet().getSelected());
    }

    @Override
    protected void updateEnabledState(Collection<? extends OsmPrimitive> selection) {
        boolean allWays = true;
        for (OsmPrimitive w : selection) {
            if (!(w instanceof Way)) {
                allWays = false;
                break;
            }

        }

        setEnabled(selection != null && selection.size() == 2 && allWays);
    }
}
