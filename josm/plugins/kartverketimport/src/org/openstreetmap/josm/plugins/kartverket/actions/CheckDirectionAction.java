// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.kartverket.actions;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.plugins.kartverket.CheckDirectionDialog;
import org.openstreetmap.josm.plugins.kartverket.CheckNextWayI;

public class CheckDirectionAction extends JosmAction implements CheckNextWayI {
    private int nWaysCompleted;
    private int nWaysFixme;
    Collection<Way> ways;
    Iterator<Way> waysIterator;
    Way w;

    public CheckDirectionAction() {
        super(tr("Check direction of streams"), null,
                tr("Check direction of streams and rivers"), null, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isEnabled())
            return;
        ways = getLayerManager().getEditDataSet().getWays();
        nWaysFixme = 0;
        nWaysCompleted = 0;
        for (Way w : ways) {
            if (w.hasTag("FIXME", "Check direction of river/stream")) {
                nWaysFixme++;
            }
        }

        waysIterator = ways.iterator();
        nextWay();

    }

    private void nextWay() {
        boolean isDone = true;

        while (waysIterator.hasNext()) {
            w = waysIterator.next();
            if (w.hasTag("FIXME", "Check direction of river/stream")) {
                getLayerManager().getEditDataSet().clearSelection();
                getLayerManager().getEditDataSet().addSelected(w.getPrimitiveId());
                BoundingXYVisitor boundingVisitor = new BoundingXYVisitor();
                boundingVisitor.visit(w);
                boundingVisitor.enlargeBoundingBoxLogarithmically();
                MainApplication.getMap().mapView.zoomTo(boundingVisitor);
                CheckDirectionDialog dialog = new CheckDirectionDialog(this, nWaysCompleted/(1.*nWaysFixme));
                dialog.makeVisible();
                isDone = false;
                break;
            }
        }
        if (isDone) {
            Notification note = new Notification(tr("No more directions to check!"));
            note.show();
        }
    }

    @Override
    public void wayDirectionIsCorrect() {
        w.remove("FIXME");
        nWaysCompleted++;
        nextWay();
    }

    @Override
    public void wayDirectionIsWrong() {
        List<Node> nd = w.getNodes();
        Collections.reverse(nd);
        w.setNodes(nd);
        wayDirectionIsCorrect();
    }

    @Override
    public void wayDirectionIgnore() {
        nWaysCompleted++;
        nextWay();
    }

    @Override
    protected void updateEnabledState() {
        if (getLayerManager().getEditDataSet() == null) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }

}
