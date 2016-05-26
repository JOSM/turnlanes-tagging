package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.bidirectional;

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
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.TurnSelection;

/**
 *
 * @author ruben
 */
public class TurnSelectionBidirectional extends JPanel {

    public static final String jTextField_CHANGED = "jTextField Changed";

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
    JPanel jpnContentSpinnerB = null;
    JSpinner spinnerB = null;

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
    JTextField jTextField = new JTextField();

    //Road    
    BRoad valBRoad = new BRoad();

    int min = 1;
    int max = 4;
    int step = 1;
    int initValue = 1;

    public TurnSelectionBidirectional() {
        super();
        init();
    }

    public void init() {
        //add on Main Panel
        setLayout(new BorderLayout());
        add(buildselect(), BorderLayout.NORTH);
        add(buildturn(), BorderLayout.CENTER);
        add(jTextField, BorderLayout.SOUTH);

        jTextField.getDocument().addDocumentListener(new SetTagTurnListenerBidirectional());
    }

    private class SetTagTurnListenerBidirectional implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            firePropertyChange(jTextField_CHANGED, null, valBRoad);
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
        jpnlSelectWardB = new JPanel(new GridLayout(1, 3));
        jchbothwayB = new JCheckBox();
        jchbothwayB.addActionListener(actionListenerB);
        jpnContentSpinnerB = new JPanel(new GridLayout(1, 1));
        spinnerB = new JSpinner(new SpinnerNumberModel(initValue, min, max, step));
        jpnContentSpinnerB.add(spinnerB);
        spinnerB.addChangeListener(new SPinnerListenerB());
        jpnlSelectWardB.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        //add compnents in B
        jpnlSelectWardB.add(new JLabel("Both Way"));
        jpnlSelectWardB.add(jchbothwayB);
        jpnlSelectWardB.add(jpnContentSpinnerB);

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
        //jpanelcontentTurns.add(jpnlturnsB, BorderLayout.CENTER);
        jpanelcontentTurns.add(jpnlturnsC, BorderLayout.EAST);
        return jpanelcontentTurns;

    }

    class SPinnerListenerA implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (jrbLaneForwardA.isSelected()) {
                lanesA("Forward Lanes", Integer.valueOf(spinnerA.getValue().toString()));
            } else {
                lanesA("Backward Lanes", Integer.valueOf(spinnerA.getValue().toString()));
            }
        }
    }

    class SPinnerListenerB implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {

            if (jchbothwayB.isSelected()) {
                lanesB("Both Ways Lanes", Integer.valueOf(spinnerB.getValue().toString()));
            } else {
                jpnlturnsB.setBorder(null);
                jpnlturnsB.removeAll();
                jpnlturnsB.revalidate();
                jpnlturnsB.repaint();
            }
        }
    }

    class SPinnerListenerC implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (jrbLaneForwardC.isSelected()) {
                lanesC("Forward Lanes", Integer.valueOf(spinnerC.getValue().toString()));
            } else {
                lanesC("Backward Lanes", Integer.valueOf(spinnerC.getValue().toString()));
            }
        }
    }

    ActionListener actionListenerA = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (jrbLaneForwardA.isSelected()) {
                lanesA("Forward Lanes", Integer.valueOf(spinnerA.getValue().toString()));
            } else {
                lanesA("Backward Lanes", Integer.valueOf(spinnerA.getValue().toString()));
            }

        }
    };

    ActionListener actionListenerB = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (jchbothwayB.isSelected()) {
                jpanelcontentTurns.add(jpnlturnsB, BorderLayout.CENTER);
                lanesB("Both Ways Lanes", Integer.valueOf(spinnerB.getValue().toString()));
            } else {
                jpnlturnsB.setBorder(null);
                jpnlturnsB.removeAll();
                jpnlturnsB.revalidate();
                jpnlturnsB.repaint();
            }
        }
    };

    ActionListener actionListenerC = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (jrbLaneForwardC.isSelected()) {
                lanesC("Forward Lanes", Integer.valueOf(spinnerC.getValue().toString()));
            } else {
                lanesC("Backward Lanes", Integer.valueOf(spinnerC.getValue().toString()));
            }
        }
    };

    public void lanesA(String type, int numLanes) {
        jpnlturnsA.setBorder(javax.swing.BorderFactory.createTitledBorder(null, type, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));
        jpnlturnsA.removeAll();
        jpnlturnsA.setLayout(new GridLayout(1, numLanes));

        final List<BLine> listBLines = new ArrayList<>();
        for (int i = 0; i < numLanes; i++) {
            BLine bLine = new BLine((i + 1), "");
            final TurnSelection turnSelection = new TurnSelection(bLine);
            turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(turnSelection.jRBLeft_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    } else if (evt.getPropertyName().equals(turnSelection.jRBRight_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    } else if (evt.getPropertyName().equals(turnSelection.jCBThrough_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    }
                }
            });
            jpnlturnsA.add(turnSelection);
        }

        jpnlturnsA.revalidate();
        jpnlturnsA.repaint();
    }

    public void lanesB(String type, int numLanes) {
        jpnlturnsB.setBorder(javax.swing.BorderFactory.createTitledBorder(null, type, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));
        jpnlturnsB.removeAll();
        jpnlturnsB.setLayout(new GridLayout(1, numLanes));

        final List<BLine> listBLines = new ArrayList<>();
        for (int i = 0; i < numLanes; i++) {
            BLine bLine = new BLine((i + 1), "");
            final TurnSelection turnSelection = new TurnSelection(bLine);
            turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(turnSelection.jRBLeft_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    } else if (evt.getPropertyName().equals(turnSelection.jRBRight_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    } else if (evt.getPropertyName().equals(turnSelection.jCBThrough_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    }
                }
            });
            jpnlturnsB.add(turnSelection);
        }

        jpnlturnsB.revalidate();
        jpnlturnsB.repaint();
    }

    public void lanesC(String type, int numLanes) {
        jpnlturnsC.setBorder(javax.swing.BorderFactory.createTitledBorder(null, type, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));
        jpnlturnsC.removeAll();
        jpnlturnsC.setLayout(new GridLayout(1, numLanes));
//        for (int i = 0; i < numLanes; i++) {
//            BLine bLine = new BLine((i + 1), "");
//            TurnSelection turnSelection = new TurnSelection(bLine);
//            jpnlturnsC.add(turnSelection);
//        }

        final List<BLine> listBLines = new ArrayList<>();
        for (int i = 0; i < numLanes; i++) {
            BLine bLine = new BLine((i + 1), "");
            final TurnSelection turnSelection = new TurnSelection(bLine);
            turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(turnSelection.jRBLeft_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    } else if (evt.getPropertyName().equals(turnSelection.jRBRight_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    } else if (evt.getPropertyName().equals(turnSelection.jCBThrough_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                        jTextField.setText(valBRoad.getTagturns());

                    }
                }
            });
            jpnlturnsC.add(turnSelection);
        }
        jpnlturnsC.revalidate();
        jpnlturnsC.repaint();
    }

    public BRoad getRoad() {
        return valBRoad;
    }

}
