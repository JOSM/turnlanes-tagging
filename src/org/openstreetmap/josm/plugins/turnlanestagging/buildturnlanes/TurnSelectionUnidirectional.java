// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLanes;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsData;
import org.openstreetmap.josm.plugins.turnlanestagging.util.Util;

/**
 *
 * @author ruben
 */
public class TurnSelectionUnidirectional extends JPanel {

    JPanel jpanelcontent = null;
    JPanel jpanelContentLane = null;
    JPanel jpanelcontentTurns = null;

    //Panel for create the number of lines.
    JPanel jpanelcontentSelections = null;
    JSpinner spinner = null;

    private final JTextField jtfChangeLanes = new JTextField();
    private final List<LaneBoundaryStrip> strips = new ArrayList<>();
    private final BikeLanePanel bikeLeftPanel = new BikeLanePanel();
    private final BikeLanePanel bikeRightPanel = new BikeLanePanel();
    private final LaneBoundaryStrip stripBikeLeft = new LaneBoundaryStrip();
    private final LaneBoundaryStrip stripBikeRight = new LaneBoundaryStrip();
    private final JPanel curbLeft = new JPanel();
    private final JPanel curbRight = new JPanel();

    // Event Changed
    public static final String LINESCHANGED = "LinesChanged";

    //To avoid automatic changes
    boolean clickLanesAction = true;

    //Road
    BRoad valBRoad = new BRoad();

    //Preset Data
    PresetsData presetsData = new PresetsData();

    int min = 1;
    int max = 12;
    int step = 1;
    int initValue = 1;
    boolean eventSpiner = true;

    //Constructor
    public TurnSelectionUnidirectional() {
        super();
        init();
    }

    public void init() {
        //add in Main Panel
        setLayout(new BorderLayout());
        add(buildselect(), BorderLayout.NORTH);
        jpanelContentLane = new JPanel(new GridLayout(1, 1));
        jpanelContentLane.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        jpanelcontentTurns = new JPanel();
        curbLeft.setOpaque(true);
        curbLeft.setBackground(new Color(175, 175, 175));
        curbLeft.setPreferredSize(new Dimension(8, 60));
        curbLeft.setMaximumSize(new Dimension(8, Integer.MAX_VALUE));
        curbRight.setOpaque(true);
        curbRight.setBackground(new Color(175, 175, 175));
        curbRight.setPreferredSize(new Dimension(8, 60));
        curbRight.setMaximumSize(new Dimension(8, Integer.MAX_VALUE));

        jpanelcontentTurns.setBorder(BorderFactory.createTitledBorder(null, tr("Lanes"),
                TitledBorder.CENTER, TitledBorder.CENTER, null, new Color(102, 102, 102)));
        JScrollPane jsp = new JScrollPane(jpanelcontentTurns);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jpanelContentLane.add(jsp);
        add(jpanelContentLane, BorderLayout.CENTER);
        //add(jtfChangeLanes, BorderLayout.SOUTH);
        //Event Road Listenr
        jtfChangeLanes.getDocument().addDocumentListener(new SetLanesChangeListener());
        jpanelcontentTurns.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    return;
                }
                Integer value = (Integer) spinner.getValue();
                value -= e.getUnitsToScroll() / 3;
                if (value <= max && value >= min) {
                    spinner.setValue(value);
                }
            }
        });
    }

    private class SetLanesChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            firePropertyChange(LINESCHANGED, null, getValBRoad());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

    }

    //Build pannel for   lanes
    protected JPanel buildselect() {

        jpanelcontentSelections = new JPanel(new GridLayout(1, 2));
        jpanelcontentSelections.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        //fill Combo Box
        spinner = new JSpinner(new SpinnerNumberModel(initValue, min, max, step));
        jpanelcontentSelections.add(new JLabel(tr("Number of lanes")));
        jpanelcontentSelections.add(spinner);
        spinner.addChangeListener(new SPinnerListener());
        spinner.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    return;
                }
                Integer value = (Integer) spinner.getValue();
                value -= e.getUnitsToScroll() / 3;
                if (value <= max && value >= min) {
                    spinner.setValue(value);
                }
            }
        });

        return jpanelcontentSelections;
    }

    public void lanes(BLanes bLanes) {
        jpanelcontentTurns.removeAll();
        //Clone objects
        valBRoad.setName("Unidirectional");
        int numLanes = bLanes.getLanes().size();
        jpanelcontentTurns.setLayout(new BoxLayout(jpanelcontentTurns, BoxLayout.X_AXIS));
        strips.clear();
        jpanelcontentTurns.add(bikeLeftPanel);
        jpanelcontentTurns.add(curbLeft);
        jpanelcontentTurns.add(stripBikeLeft);

        eventSpiner = false;
        spinner.setValue(numLanes);
        eventSpiner = true;

        valBRoad.setLanesUnid(bLanes);

        final List<BLane> listBLines = valBRoad.getLanesUnid().getLanes();
        for (int i = 0; i < numLanes; i++) {
            BLane bLine = listBLines.get(i);
            if (i > 0) {
                LaneBoundaryStrip strip = new LaneBoundaryStrip();
                strips.add(strip);
                jpanelcontentTurns.add(strip);
            }
            final TurnSelection turnSelection = new TurnSelection(bLine, numLanes, false);
            turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(TurnSelection.Left_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.Right_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.Through_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.Slight_right_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.Slight_left_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.Merge_to_right_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.Merge_to_left_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.Reverse_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.CHANGE_CHANGED)
                            || evt.getPropertyName().equals(TurnSelection.BIKE_CHANGED)) {
                        listBLines.add((BLane) evt.getNewValue());
                        valBRoad.getLanesUnid().setLanes(listBLines);
                        jtfChangeLanes.setText(valBRoad.getLanesUnid().getTagturns());
                        refreshBoundaries();
                        if (evt.getPropertyName().equals(TurnSelection.CHANGE_CHANGED)) {
                            warnIfIncompleteChange();
                        }
                    }
                }
            });
            jpanelcontentTurns.add(turnSelection);
        }
        jpanelcontentTurns.add(stripBikeRight);
        jpanelcontentTurns.add(curbRight);
        jpanelcontentTurns.add(bikeRightPanel);
        jtfChangeLanes.setText(valBRoad.getLanesUnid().getTagturns());
        refreshBoundaries();
        jpanelcontentTurns.revalidate();
        jpanelcontentTurns.repaint();
    }

    public void setDefault(BRoad bRoad) {
        lanes(bRoad.getLanesUnid());
        valBRoad.setDivider(bRoad.getDivider());
        valBRoad.setLaneMarkings(bRoad.getLaneMarkings());
        valBRoad.setCyclewayLeft(bRoad.getCyclewayLeft());
        valBRoad.setCyclewayRight(bRoad.getCyclewayRight());
        refreshPreview();
    }

    public BRoad getValBRoad() {
        return valBRoad;
    }

    /**
     * Repaints the boundary strips after road-level options (divider, lane
     * markings, bicycle lanes) change without a full lane rebuild.
     */
    public void refreshPreview() {
        valBRoad.setDivider(BuildTurnLanes.bRoad.getDivider());
        valBRoad.setLaneMarkings(BuildTurnLanes.bRoad.getLaneMarkings());
        valBRoad.setCyclewayLeft(BuildTurnLanes.bRoad.getCyclewayLeft());
        valBRoad.setCyclewayRight(BuildTurnLanes.bRoad.getCyclewayRight());
        refreshBoundaries();
    }

    /**
     * Updates the boundary strips between the lane panels from the current
     * lane-change restrictions and road options.
     */
    private void refreshBoundaries() {
        List<BLane> lanes = valBRoad.getLanesUnid().getLanes();
        String divider = valBRoad.getDivider();
        boolean laneMarkings = !"no".equals(valBRoad.getLaneMarkings());
        for (int i = 0; i < strips.size() && i + 1 < lanes.size(); i++) {
            BLane left = lanes.get(i);
            BLane right = lanes.get(i + 1);
            strips.get(i).setState(Util.blocksLargerOffsetSide(left),
                    Util.blocksSmallerOffsetSide(right), false, divider, laneMarkings);
        }
        String cyclewayLeft = valBRoad.getCyclewayLeft() == null ? "" : valBRoad.getCyclewayLeft();
        String cyclewayRight = valBRoad.getCyclewayRight() == null ? "" : valBRoad.getCyclewayRight();
        boolean bikeLeft = !cyclewayLeft.isEmpty() && !"no".equals(cyclewayLeft);
        boolean bikeRight = !cyclewayRight.isEmpty() && !"no".equals(cyclewayRight);
        bikeLeftPanel.setVisible(bikeLeft);
        stripBikeLeft.setVisible(bikeLeft);
        curbLeft.setVisible("track".equals(cyclewayLeft));
        bikeRightPanel.setVisible(bikeRight);
        stripBikeRight.setVisible(bikeRight);
        curbRight.setVisible("track".equals(cyclewayRight));
        if (bikeLeft && !lanes.isEmpty()) {
            bikeLeftPanel.setMode(cyclewayLeft);
            // A cycleway lane is always separated from the general traffic
            // lanes by a solid line, independent of change:lanes.
            stripBikeLeft.setState(true, true, false, divider, laneMarkings);
        }
        if (bikeRight && !lanes.isEmpty()) {
            bikeRightPanel.setMode(cyclewayRight);
            stripBikeRight.setState(true, true, false, divider, laneMarkings);
        }
        jpanelcontentTurns.revalidate();
        jpanelcontentTurns.repaint();
    }

    private void warnIfIncompleteChange() {
        Util.warnIfEmptyChangeLane(valBRoad.getLanesUnid().getTagchanges());
    }

    class SPinnerListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int lanes = Integer.valueOf(spinner.getValue().toString());
            valBRoad.setName("Unidirectional");
            BLanes bLanes = new BLanes("unid");
            if (eventSpiner) {
                if (lanes >= valBRoad.getLanesUnid().getLanes().size()) {
                    bLanes = presetsData.addLanes((BLanes) Util.clone(valBRoad.getLanesUnid()),
                            "unid", lanes - valBRoad.getLanesUnid().getLanes().size());
                } else {
                    bLanes = presetsData.removeLanes((BLanes) Util.clone(valBRoad.getLanesUnid()),
                            valBRoad.getLanesUnid().getLanes().size() - lanes);
                }
                lanes(bLanes);
            }
            eventSpiner = true;
        }
    }

}
