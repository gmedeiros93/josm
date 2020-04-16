// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.kartverket;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.tools.ImageProvider;

public class CheckDirectionDialog extends JDialog {
    private StopCheck stop;
    private WayDirectionCorrect wayCorrect;
    private WayDirectionWrong wayWrong;
    private WayDirectionIgnore wayIgnore;
    private SideButton defaultButton;

    public CheckDirectionDialog(CheckNextWayI checkNextWay, double progress) {
        super(JOptionPane.getFrameForComponent(MainApplication.getMainFrame()), false);
        stop = new StopCheck();
        wayCorrect = new WayDirectionCorrect(checkNextWay);
        wayWrong = new WayDirectionWrong(checkNextWay);
        wayIgnore = new WayDirectionIgnore(checkNextWay);

        build(progress);
    }

    public void makeVisible() {
        setVisible(true);
        defaultButton.requestFocus();
    }

    protected void build(double progress) {
        setLayout(new BorderLayout());

        setTitle(tr("Check the direction"));

        DecimalFormat df = new DecimalFormat("#");
        JLabel description = new JLabel(tr(tr("Check the direction of the selected stream/river. Is it correct?\n Progress: "))
                + df.format(progress*100)+ " %");
        add(description, BorderLayout.CENTER);


        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER));
        SideButton btn = new SideButton(wayCorrect);
        btn.setName(tr("Correct"));
        pnl.add(btn);
        defaultButton = btn;
        /* int c = JComponent.WHEN_IN_FOCUSED_WINDOW;
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_C,
                                    InputEvent.CTRL_DOWN_MASK);
        pnl.getInputMap(c).put(ks, "PRESS");
        pnl.getActionMap().put("PRESS", btn.getAction());
         */

        btn = new SideButton(wayWrong);
        btn.setName(tr("Wrong"));
        pnl.add(btn);
        /*ks = KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK);
        pnl.getInputMap(c).put(ks, "PRESS");
        pnl.getActionMap().put("PRESS", btn.getAction());
         */

        btn = new SideButton(wayIgnore);
        btn.setName(tr("Ignore"));
        pnl.add(btn);
        /*ks = KeyStroke.getKeyStroke(KeyEvent.VK_I,
                InputEvent.CTRL_DOWN_MASK);
        pnl.getInputMap(c).put(ks, "PRESS");
        pnl.getActionMap().put("PRESS", btn.getAction());
         */

        btn = new SideButton(stop);
        btn.setName(tr("Stop"));
        pnl.add(btn);

        add(pnl, BorderLayout.SOUTH);

        Point p = new Point(200, 100);
        setLocation(p);
        Dimension minimumSize = new Dimension(500, 120);
        setMinimumSize(minimumSize);
    }

    class StopCheck extends AbstractAction {
        StopCheck() {
            putValue(NAME, tr("Close"));
            putValue(SHORT_DESCRIPTION, tr("Close the dialog"));
            new ImageProvider("cancel").getResource().attachImageIcon(this, true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    class WayDirectionCorrect extends AbstractAction {
        CheckNextWayI parent;

        WayDirectionCorrect(CheckNextWayI parent) {
            putValue(NAME, tr("<HTML><U>C</U>orrect direction</HTML>"));
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(SHORT_DESCRIPTION, tr("Direction of river is correct."));
            new ImageProvider("ok").getResource().attachImageIcon(this, true);
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            parent.wayDirectionIsCorrect();
            setVisible(false);
        }
    }

    class WayDirectionWrong extends AbstractAction {
        CheckNextWayI parent;
        WayDirectionWrong(CheckNextWayI parent) {
            putValue(NAME, tr("<HTML><U>W</U>rong dirrection</HTML>"));
            putValue(MNEMONIC_KEY, KeyEvent.VK_W);
            putValue(SHORT_DESCRIPTION, tr("Direction of river is wrong."));
            new ImageProvider("wayflip").getResource().attachImageIcon(this, true);
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            parent.wayDirectionIsWrong();
            setVisible(false);
        }
    }

    class WayDirectionIgnore extends AbstractAction {
        CheckNextWayI parent;
        WayDirectionIgnore(CheckNextWayI parent) {
            putValue(NAME, tr("<HTML><U>I</U>gnore this way</HTML>"));
            putValue(SHORT_DESCRIPTION, tr("Ignore direction of this way."));
            new ImageProvider("redo").getResource().attachImageIcon(this, true);
            putValue(MNEMONIC_KEY, KeyEvent.VK_I);
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            parent.wayDirectionIgnore();
            setVisible(false);
        }
    }

}
