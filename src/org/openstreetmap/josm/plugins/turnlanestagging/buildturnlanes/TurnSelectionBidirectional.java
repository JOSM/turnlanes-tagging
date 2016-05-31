package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLanes;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsData;

/**
 *
 * @author ruben
 */
public class TurnSelectionBidirectional extends JPanel {

    public static final String LINESCHANGEDBIDIRECTIONAL = "BidirectionalLinesChanged";

    JPanel jpanelcontent = null;
    JPanel jpanelcontentSelections = null;
    JPanel jpanelcontentTurns = null;

    //A
    JPanel jpanelcontentA = null;
    private JPanel jpnlSelectWardA = null;
    private JPanel jpnlturnsA = null;
    private ButtonGroup bgWardA = null;
    private JRadioButton jrbLaneForwardA = null;
    private JRadioButton jrbLaneBackwardA = null;
    JPanel jpnContentSpinnerA = null;
    JSpinner spinnerA = null;
    //B
    JPanel jpanelcontentB = null;
    private JPanel jpnlSelectWardB = null;
    private JPanel jpnlturnsB = null;
    private JCheckBox jchbothwayB = null;
    //C
    JPanel jpanelcontentC = null;
    private JPanel jpnlSelectWardC = null;
    private JPanel jpnlturnsC = null;
    private ButtonGroup bgWardC = null;
    private JRadioButton jrbLaneForwardC = null;
    private JRadioButton jrbLaneBackwardC = null;
    JPanel jpnContentSpinnerC = null;
    JSpinner spinnerC = null;

    //Jtext
    private final JTextField jtfChangeLanes = new JTextField();

    //Values road
    BRoad valBRoad = new BRoad();
    BLanes bLanesA = new BLanes();
    BLanes bLanesB = new BLanes();
    BLanes bLanesC = new BLanes();

    //Preset Data
    PresetsData presetsData = new PresetsData();
    int min = 0;
    int max = 10;
    int step = 1;
    int initValue = 1;

    //Avoid event after change spiner
    boolean eventSpinerA = true;
    boolean eventSpinerC = true;

    public TurnSelectionBidirectional() {
        super();
        init();
    }

    public void init() {
        //add on Main Panel
        setLayout(new BorderLayout());
        add(buildselect(), BorderLayout.NORTH);
        add(buildturn(), BorderLayout.CENTER);
        //add(jtfChangeLanes, BorderLayout.SOUTH);
        jtfChangeLanes.getDocument().addDocumentListener(new SetLanesChangeListener());
    }

    //Action Liseners when the roads changes
    private class SetLanesChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {

            firePropertyChange(LINESCHANGEDBIDIRECTIONAL, null, valBRoad);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

    }

    public JPanel buildselect() {

        jpanelcontent = new JPanel(new GridLayout(2, 1));
        jpanelcontentSelections = new JPanel(new GridLayout(1, 3, 10, 10));
        // A
        jpanelcontentA = new JPanel(new GridLayout(1, 1));
        jpnlSelectWardA = new JPanel(new GridLayout(1, 3));
        bgWardA = new ButtonGroup();
        jrbLaneForwardA = new JRadioButton("Forward");
        jrbLaneBackwardA = new JRadioButton("Backward");
        bgWardA.add(jrbLaneForwardA);
        bgWardA.add(jrbLaneBackwardA);
        jrbLaneForwardA.addActionListener(actionListenerA);
        jrbLaneBackwardA.addActionListener(actionListenerA);

        jpnContentSpinnerA = new JPanel(new GridLayout(1, 1));
        spinnerA = new JSpinner(new SpinnerNumberModel(initValue, min, max, step));
        jpnContentSpinnerA.add(spinnerA);
        spinnerA.addChangeListener(new SPinnerListenerA());

        //add compnents in A
        jpnlSelectWardA.add(jrbLaneForwardA);
        jpnlSelectWardA.add(jrbLaneBackwardA);
        jpnlSelectWardA.add(jpnContentSpinnerA);
        jpnlSelectWardA.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        jpnlturnsA = new JPanel();
        jpanelcontentA.add(jpnlSelectWardA);

        //B
        jpanelcontentB = new JPanel(new GridLayout(1, 1));
        jpnlSelectWardB = new JPanel(new GridLayout(1, 2));
        jchbothwayB = new JCheckBox();
        jchbothwayB.addActionListener(actionListenerB);

        jpnlSelectWardB.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        //add compnents in B
        jpnlSelectWardB.add(new JLabel("Both Way"));
        jpnlSelectWardB.add(jchbothwayB);

        jpnlturnsB = new JPanel();
        jpanelcontentB.add(jpnlSelectWardB);

        // C
        jpanelcontentC = new JPanel(new GridLayout(1, 1));
        jpnlSelectWardC = new JPanel(new GridLayout(1, 3));
        bgWardC = new ButtonGroup();
        jrbLaneForwardC = new JRadioButton("Forward");
        jrbLaneBackwardC = new JRadioButton("Backward");
        bgWardC.add(jrbLaneForwardC);
        bgWardC.add(jrbLaneBackwardC);

        jrbLaneForwardC.addActionListener(actionListenerC);
        jrbLaneBackwardC.addActionListener(actionListenerC);
        jpnContentSpinnerC = new JPanel(new GridLayout(1, 1));

        spinnerC = new JSpinner(new SpinnerNumberModel(initValue, min, max, step));
        jpnContentSpinnerC.add(spinnerC);
        spinnerC.addChangeListener(new SPinnerListenerC());
        jpnlSelectWardC.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        //add compnents in C
        jpnlSelectWardC.add(jrbLaneForwardC);
        jpnlSelectWardC.add(jrbLaneBackwardC);
        jpnlSelectWardC.add(jpnContentSpinnerC);

        jpnlturnsC = new JPanel();
        jpanelcontentC.add(jpnlSelectWardC);

        //Add all Selection panels
        jpanelcontentSelections.add(jpanelcontentA);
        jpanelcontentSelections.add(jpanelcontentB);
        jpanelcontentSelections.add(jpanelcontentC);

        //Add All turns lanes
        jpanelcontent.add(jpanelcontentSelections);
        return jpanelcontent;

    }

    public JPanel buildturn() {
        jpanelcontentTurns = new JPanel();
        jpanelcontentTurns.setLayout(new BorderLayout());
        jpanelcontentTurns.add(jpnlturnsA, BorderLayout.WEST);
        jpanelcontentTurns.add(jpnlturnsB, BorderLayout.CENTER);
        jpanelcontentTurns.add(jpnlturnsC, BorderLayout.EAST);
        return jpanelcontentTurns;
    }

    class SPinnerListenerA implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            listenerA();
        }
    }

    class SPinnerListenerC implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            listenerC();
        }
    }

    ActionListener actionListenerA = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            listenerA();
        }
    };

    ActionListener actionListenerB = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            listenerB();
        }
    };

    ActionListener actionListenerC = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            listenerC();
        }
    };

    public void lanesA(BLanes bLanes) {
        jpnlturnsA.setBorder(null);
        jpnlturnsA.removeAll();
        jpnlturnsA.revalidate();
        jpnlturnsA.repaint();
        jrbLaneForwardA.setSelected(false);
        jrbLaneBackwardA.setSelected(false);
        //change without event
        eventSpinerA = false;
        spinnerA.setValue(bLanes.getLanes().size());
        if (bLanes.getLanes().size() > 0) {
            if (bLanes.getType().equals("forward")) {
                jrbLaneForwardA.setSelected(true);
            } else {
                jrbLaneBackwardA.setSelected(true);
            }
            jpnlturnsA.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bLanes.getType(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));
            jpnlturnsA.removeAll();
            //Clone objtects
            bLanesA.setType(bLanes.getType());
            List<BLane> listbl = new ArrayList<>();
            for (int k = 0; k < bLanes.getLanes().size(); k++) {
                BLane bl = new BLane(new String(bLanes.getType()), new Integer(bLanes.getLanes().get(k).getPosition()), new String(bLanes.getLanes().get(k).getTurn()));
                listbl.add(bl);
            }
            bLanesA.setLanes(listbl);
            int numLanes = bLanesA.getLanes().size();
            jpnlturnsA.setLayout(new GridLayout(1, numLanes));
            final List<BLane> listBLanes = bLanesA.getLanes();
            for (int i = 0; i < numLanes; i++) {
                BLane bLine = listBLanes.get(i);
                final TurnSelection turnSelection = new TurnSelection(bLine);
                turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals(TurnSelection.jRBLeft_CHANGED) || evt.getPropertyName().equals(TurnSelection.jRBRight_CHANGED) || evt.getPropertyName().equals(TurnSelection.jCBThrough_CHANGED)) {
                            listBLanes.add((BLane) evt.getNewValue());
                            bLanesA.setLanes(listBLanes);
                            printChageLanes();
                        }
                    }
                });
                jpnlturnsA.add(turnSelection);
            }
            jpnlturnsA.revalidate();
            jpnlturnsA.repaint();
        }
    }

    public void lanesB(BLanes bLanes) {
        jpnlturnsB.setBorder(null);
        jpnlturnsB.removeAll();
        jpnlturnsB.revalidate();
        jpnlturnsB.repaint();
        jchbothwayB.setSelected(false);
        if (bLanes.getLanes().size() > 0) {
            if (bLanes.getType().equals("both_ways")) {
                jchbothwayB.setSelected(true);
            }
            jpnlturnsB.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bLanes.getType(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));
            //Clone objtects
            bLanesB.setType(bLanes.getType());
            List<BLane> listbl = new ArrayList<>();
            for (int k = 0; k < bLanes.getLanes().size(); k++) {
                BLane bl = new BLane(new String(bLanes.getType()), new Integer(bLanes.getLanes().get(k).getPosition()), new String(bLanes.getLanes().get(k).getTurn()));
                listbl.add(bl);
            }
            bLanesB.setLanes(listbl);
            int numLanes = bLanesB.getLanes().size();
            jpnlturnsB.setLayout(new GridLayout(1, numLanes));
            final List<BLane> listBLanes = bLanesB.getLanes();
            for (int i = 0; i < numLanes; i++) {
                BLane bLine = listBLanes.get(i);
                final TurnSelection turnSelection = new TurnSelection(bLine);
                turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals(TurnSelection.jRBLeft_CHANGED) || evt.getPropertyName().equals(TurnSelection.jRBRight_CHANGED) || evt.getPropertyName().equals(TurnSelection.jCBThrough_CHANGED)) {
                            listBLanes.add((BLane) evt.getNewValue());
                            bLanesB.setLanes(listBLanes);
                            printChageLanes();
                        }
                    }
                });
                jpnlturnsB.add(turnSelection);
            }
            jpnlturnsB.revalidate();
            jpnlturnsB.repaint();
        }
    }

    public void lanesC(BLanes bLanes) {
        jpnlturnsC.setBorder(null);
        jpnlturnsC.removeAll();
        jpnlturnsC.revalidate();
        jpnlturnsC.repaint();
        jrbLaneForwardC.setSelected(false);
        jrbLaneBackwardC.setSelected(false);
        //change without event
        eventSpinerC = false;
        spinnerC.setValue(bLanes.getLanes().size());
        if (bLanes.getLanes().size() > 0) {
            if (bLanes.getType().equals("forward")) {
                jrbLaneForwardC.setSelected(true);
            } else {
                jrbLaneBackwardC.setSelected(true);
            }
            jpnlturnsC.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bLanes.getType(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));
            jpnlturnsC.removeAll();
            //Clone objtects
            bLanesC.setType(bLanes.getType());
            List<BLane> listbl = new ArrayList<>();
            for (int k = 0; k < bLanes.getLanes().size(); k++) {
                BLane bl = new BLane(new String(bLanes.getType()), new Integer(bLanes.getLanes().get(k).getPosition()), new String(bLanes.getLanes().get(k).getTurn()));
                listbl.add(bl);
            }
            bLanesC.setLanes(listbl);
            int numLanes = bLanesC.getLanes().size();
            jpnlturnsC.setLayout(new GridLayout(1, numLanes));
            final List<BLane> listBLanes = bLanesC.getLanes();
            for (int i = 0; i < numLanes; i++) {
                BLane bLine = listBLanes.get(i);
                final TurnSelection turnSelection = new TurnSelection(bLine);
                turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals(TurnSelection.jRBLeft_CHANGED) || evt.getPropertyName().equals(TurnSelection.jRBRight_CHANGED) || evt.getPropertyName().equals(TurnSelection.jCBThrough_CHANGED)) {
                            listBLanes.add((BLane) evt.getNewValue());
                            bLanesC.setLanes(listBLanes);
                            printChageLanes();
                        }
                    }
                });
                jpnlturnsC.add(turnSelection);
            }
            jpnlturnsC.revalidate();
            jpnlturnsC.repaint();
        }
    }

    public void printChageLanes() {
        valBRoad.setLanes("Bidirectional");
        valBRoad.setLanesA(bLanesA);
        valBRoad.setLanesB(bLanesB);
        valBRoad.setLanesC(bLanesC);
        jtfChangeLanes.setText(bLanesA.getTagturns() + "==" + bLanesB.getTagturns() + "==" + bLanesC.getTagturns());
    }

    public void setDefault(BRoad bRoad) {
        valBRoad.setLanes("Bidirectional");
        bLanesA = bRoad.getLanesA();
        bLanesB = bRoad.getLanesB();
        bLanesC = bRoad.getLanesC();
        valBRoad.setLanesA(bLanesA);
        valBRoad.setLanesB(bLanesB);
        valBRoad.setLanesC(bLanesC);
        jtfChangeLanes.setText(bLanesA.getTagturns() + "==" + bLanesB.getTagturns() + "==" + bLanesC.getTagturns());
        lanesA(bRoad.getLanesA());
        lanesB(bRoad.getLanesB());
        lanesC(bRoad.getLanesC());
    }

    private void listenerA() {
        if (eventSpinerA) {
            if (jrbLaneForwardA.isSelected()) {
                BLanes bLanes = presetsData.defaultLanes("forward", Integer.valueOf(spinnerA.getValue().toString()));
                bLanes.setType("forward");
                lanesA(bLanes);
            } else {
                BLanes bLanes = presetsData.defaultLanes("backward", Integer.valueOf(spinnerA.getValue().toString()));
                bLanes.setType("backward");
                lanesA(bLanes);
            }
        }
        eventSpinerA = true;
    }

    private void listenerB() {
        BLanes bLanes = presetsData.defaultLanes("both_ways", 1);
        bLanes.setType("both_ways");
        if (jchbothwayB.isSelected()) {
            jpanelcontentTurns.add(jpnlturnsB, BorderLayout.CENTER);
            lanesB(bLanes);
        } else {
            jpnlturnsB.setBorder(null);
            jpnlturnsB.removeAll();
            jpnlturnsB.revalidate();
            jpnlturnsB.repaint();
        }
    }

    private void listenerC() {
        if (eventSpinerC) {
            if (jrbLaneForwardC.isSelected()) {
                BLanes bLanes = presetsData.defaultLanes("forward", Integer.valueOf(spinnerC.getValue().toString()));
                bLanes.setType("forward");
                lanesC(bLanes);
            } else {
                BLanes bLanes = presetsData.defaultLanes("backward", Integer.valueOf(spinnerC.getValue().toString()));
                bLanes.setType("backward");
                lanesC(bLanes);
            }

        }
        eventSpinerC = true;
    }
}
