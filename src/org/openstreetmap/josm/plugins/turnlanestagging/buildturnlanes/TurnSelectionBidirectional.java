// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
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
public class TurnSelectionBidirectional extends JPanel {

    public static final String LINESCHANGEDBIDIRECTIONAL = "BidirectionalLinesChanged";
    JPanel jpanelcontent = null;
    JPanel jpanelcontentSelections = null;
    JPanel jpanelcontentTurns = null;
    //A
    JPanel jpanelcontentA = null;
    private JPanel jpnlSelectWardA = null;
    private JPanel jpnlturnsA = null;
    private JLabel labelA = null;
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
    private JLabel labelC = null;
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
    int min = 1;
    int max = 10;
    int step = 1;
    int initValue = 1;
    //Avoid event after change spiner
    boolean eventSpinerA = true;
    boolean eventSpinerC = true;
    GridBagConstraints gbc = new GridBagConstraints();

    public TurnSelectionBidirectional() {
        super();
        init();
    }

    public void init() {
        //add on Main Panel
        setLayout(new BorderLayout());
        add(buildselect(), BorderLayout.NORTH);
        JScrollPane jsp = new JScrollPane(buildturn());
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(jsp, BorderLayout.CENTER);
        //add(jtfChangeLanes, BorderLayout.SOUTH);
        jtfChangeLanes.getDocument().addDocumentListener(new SetLanesChangeListener());
        jpnlturnsA.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    return;
                }
                Integer value = (Integer) spinnerA.getValue();
                value -= e.getUnitsToScroll() / 3;
                if (value <= max && value >= min) {
                    spinnerA.setValue(value);
                }
            }
        });
        jpnlturnsC.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    return;
                }
                Integer value = (Integer) spinnerC.getValue();
                value -= e.getUnitsToScroll() / 3;
                if (value <= max && value >= min) {
                    spinnerC.setValue(value);
                }
            }
        });
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

        jpanelcontent = new JPanel(new GridLayout(1, 1));
        jpanelcontentSelections = new JPanel(new GridBagLayout());
        // A
        jpanelcontentA = new JPanel(new GridLayout(1, 1));
        jpnlSelectWardA = new JPanel(new BorderLayout(5, 5));
        jpnContentSpinnerA = new JPanel(new GridLayout(1, 1));
        spinnerA = new JSpinner(new SpinnerNumberModel(initValue, min, max, step));
        jpnContentSpinnerA.add(spinnerA);
        spinnerA.addChangeListener(new SPinnerListenerA());
        spinnerA.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    return;
                }
                Integer value = (Integer) spinnerA.getValue();
                value -= e.getUnitsToScroll() / 3;
                if (value <= max && value >= min) {
                    spinnerA.setValue(value);
                }
            }
        });

        labelA = new JLabel(tr("Forward"));
        jpnlSelectWardA.add(labelA, BorderLayout.LINE_START);
        jpnlSelectWardA.add(jpnContentSpinnerA, BorderLayout.LINE_END);
        jpnlSelectWardA.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        jpnlturnsA = new JPanel();
        jpanelcontentA.add(jpnlSelectWardA);
        //B
        jpanelcontentB = new JPanel(new GridLayout(1, 1));
        jpnlSelectWardB = new JPanel(new GridBagLayout());
        jchbothwayB = new JCheckBox();
        jchbothwayB.addActionListener(actionListenerB);
        jpnlSelectWardB.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        //add compnents in B
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpnlSelectWardB.add(new JLabel(tr("Both way lane")), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpnlSelectWardB.add(jchbothwayB, gbc);
        jpnlturnsB = new JPanel();
        jpanelcontentB.add(jpnlSelectWardB);
        // C
        jpanelcontentC = new JPanel(new GridLayout(1, 1));
        jpnlSelectWardC = new JPanel(new BorderLayout(5, 5));
        jpnContentSpinnerC = new JPanel(new GridLayout(1, 1));
        spinnerC = new JSpinner(new SpinnerNumberModel(initValue, min, max, step));
        jpnContentSpinnerC.add(spinnerC);
        spinnerC.addChangeListener(new SPinnerListenerC());
        spinnerC.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    return;
                }
                Integer value = (Integer) spinnerC.getValue();
                value -= e.getUnitsToScroll() / 3;
                if (value <= max && value >= min) {
                    spinnerC.setValue(value);
                }
            }
        });

        jpnlSelectWardC.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        labelC = new JLabel(tr("Backward"));
        jpnlSelectWardC.add(labelC, BorderLayout.LINE_START);
        jpnlSelectWardC.add(jpnContentSpinnerC, BorderLayout.LINE_END);
        jpnlturnsC = new JPanel();
        jpanelcontentC.add(jpnlSelectWardC);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        jpanelcontentSelections.add(jpanelcontentA, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        jpanelcontentSelections.add(jpanelcontentB, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanelcontentSelections.add(jpanelcontentC, gbc);

        //Add All turns lanes
        jpanelcontent.add(jpanelcontentSelections);
        return jpanelcontent;
    }

    public JPanel buildturn() {
        jpanelcontentTurns = new JPanel();
        jpanelcontentTurns.setLayout(new BorderLayout());
        jpanelcontentTurns.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
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

        //change without event
        eventSpinerA = false;
        spinnerA.setValue(bLanes.getLanes().size());
        eventSpinerA = true;
        if (bLanes.getLanes().size() > 0) {
            if (Util.isRightHandTraffic()) {
                labelA.setText(tr("Number of backward lanes"));
                bLanes.setType("backward");
                jpnlturnsA.setBorder(BorderFactory.createTitledBorder(null, tr("Backward"),
                        TitledBorder.CENTER, TitledBorder.CENTER, null, new Color(102, 102, 102)));
                jpnlturnsA.removeAll();
                int numLanes = bLanesA.getLanes().size();
                jpnlturnsA.setLayout(new GridLayout(1, numLanes));
                final List<BLane> listBLanes = bLanesA.getLanes();
                for (int i = numLanes - 1; i >= 0; i--) {
                    BLane bLine = listBLanes.get(i);
                    final TurnSelection turnSelection = new TurnSelection(bLine, numLanes, Util.isRightHandTraffic());
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
                                    || evt.getPropertyName().equals(TurnSelection.Reverse_CHANGED)) {
                                listBLanes.add((BLane) evt.getNewValue());
                                bLanesA.setLanes(listBLanes);
                                printChageLanes();
                            }
                        }
                    });
                    jpnlturnsA.add(turnSelection);
                }
            } else {
                labelA.setText(tr("Number of forward lanes"));
                bLanes.setType("forward");
                jpnlturnsA.setBorder(BorderFactory.createTitledBorder(null, tr("Forward"),
                        TitledBorder.CENTER, TitledBorder.CENTER, null, new Color(102, 102, 102)));
                jpnlturnsA.removeAll();
                int numLanes = bLanesA.getLanes().size();
                jpnlturnsA.setLayout(new GridLayout(1, numLanes));
                final List<BLane> listBLanes = bLanesA.getLanes();
                for (int i = 0; i < numLanes; i++) {
                    BLane bLine = listBLanes.get(i);
                    final TurnSelection turnSelection = new TurnSelection(bLine, numLanes, Util.isRightHandTraffic());
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
                                    || evt.getPropertyName().equals(TurnSelection.Reverse_CHANGED)) {
                                listBLanes.add((BLane) evt.getNewValue());
                                bLanesA.setLanes(listBLanes);
                                printChageLanes();
                            }
                        }
                    });
                    jpnlturnsA.add(turnSelection);
                }

            }
            printChageLanes();
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
            jpnlturnsB.setBorder(BorderFactory.createTitledBorder(null, tr("Both way"),
                    TitledBorder.CENTER, TitledBorder.CENTER, null, new Color(102, 102, 102)));
            bLanesB.setType(bLanes.getType());
            int numLanes = bLanesB.getLanes().size();
            jpnlturnsB.setLayout(new GridLayout(1, numLanes));
            final List<BLane> listBLanes = bLanesB.getLanes();
            for (int i = 0; i < numLanes; i++) {
                BLane bLine = listBLanes.get(i);
                final TurnSelection turnSelection = new TurnSelection(bLine, numLanes, Util.isRightHandTraffic());
                turnSelection.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals(TurnSelection.Left_CHANGED)
                                || evt.getPropertyName().equals(TurnSelection.Right_CHANGED)
                                || evt.getPropertyName().equals(TurnSelection.Reversible_CHANGED)) {
                            listBLanes.add((BLane) evt.getNewValue());
                            bLanesB.setLanes(listBLanes);
                            printChageLanes();
                        }
                    }
                });
                jpnlturnsB.add(turnSelection);
            }
            printChageLanes();
            jpnlturnsB.revalidate();
            jpnlturnsB.repaint();
        }
        printChageLanes();
    }

    public void lanesC(BLanes bLanes) {
        jpnlturnsC.setBorder(null);
        jpnlturnsC.removeAll();
        jpnlturnsC.revalidate();
        jpnlturnsC.repaint();

        //change without event
        eventSpinerC = false;
        spinnerC.setValue(bLanes.getLanes().size());
        eventSpinerC = true;

        if (bLanes.getLanes().size() > 0) {
            if (Util.isRightHandTraffic()) {
                labelC.setText(tr("Number of forward lanes"));
                bLanes.setType("forward");
                jpnlturnsC.setBorder(BorderFactory.createTitledBorder(null, tr("Forward"),
                        TitledBorder.CENTER, TitledBorder.CENTER, null, new Color(102, 102, 102)));
                jpnlturnsC.removeAll();
                int numLanes = bLanesC.getLanes().size();
                jpnlturnsC.setLayout(new GridLayout(1, numLanes));
                final List<BLane> listBLanes = bLanesC.getLanes();
                for (int i = 0; i < numLanes; i++) {
                    BLane bLine = listBLanes.get(i);
                    final TurnSelection turnSelection = new TurnSelection(bLine, numLanes, Util.isRightHandTraffic());
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
                                    || evt.getPropertyName().equals(TurnSelection.Reverse_CHANGED)) {
                                listBLanes.add((BLane) evt.getNewValue());
                                bLanesC.setLanes(listBLanes);
                                printChageLanes();
                            }
                        }
                    });
                    jpnlturnsC.add(turnSelection);
                }
            } else {
                labelC.setText(tr("Number of backward lanes"));
                bLanes.setType("backward");
                jpnlturnsC.setBorder(BorderFactory.createTitledBorder(null, tr("Backward"),
                        TitledBorder.CENTER, TitledBorder.CENTER, null, new Color(102, 102, 102)));
                jpnlturnsC.removeAll();
                int numLanes = bLanesC.getLanes().size();
                jpnlturnsC.setLayout(new GridLayout(1, numLanes));
                final List<BLane> listBLanes = bLanesC.getLanes();
                for (int i = numLanes - 1; i >= 0; i--) {
                    BLane bLine = listBLanes.get(i);
                    final TurnSelection turnSelection = new TurnSelection(bLine, numLanes, Util.isRightHandTraffic());
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
                                    || evt.getPropertyName().equals(TurnSelection.Reverse_CHANGED)) {
                                listBLanes.add((BLane) evt.getNewValue());
                                bLanesC.setLanes(listBLanes);
                                printChageLanes();
                            }
                        }
                    });
                    jpnlturnsC.add(turnSelection);
                }
            }
            //after preset the actions to update
            printChageLanes();
            jpnlturnsC.revalidate();
            jpnlturnsC.repaint();
        }
    }

    public void printChageLanes() {
        valBRoad.setName("Bidirectional");
        valBRoad.setLanesA(bLanesA);
        valBRoad.setLanesB(bLanesB);
        valBRoad.setLanesC(bLanesC);
        jtfChangeLanes.setText(bLanesA.getTagturns() + "==" + bLanesB.getTagturns() + "==" + bLanesC.getTagturns());
    }

    public void setDefault(BRoad bRoad) {
        valBRoad.setName("Bidirectional");
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
        int lanes = Integer.valueOf(spinnerA.getValue().toString());
        if (eventSpinerA) {
            if (Util.isRightHandTraffic()) {
                if (lanes >= bLanesA.getLanes().size()) {
                    bLanesA = presetsData.addLanes((BLanes) Util.clone(bLanesA), "backward", lanes - bLanesA.getLanes().size());
                } else {
                    bLanesA = presetsData.removeLanes((BLanes) Util.clone(bLanesA), bLanesA.getLanes().size() - lanes);
                }
                bLanesA.setType("backward");
                lanesA(bLanesA);
                bLanesC.setType("forward");
                lanesC(bLanesC);
            } else {
                if (lanes >= bLanesA.getLanes().size()) {
                    bLanesA = presetsData.addLanes((BLanes) Util.clone(bLanesA), "forward", lanes - bLanesA.getLanes().size());
                } else {
                    bLanesA = presetsData.removeLanes((BLanes) Util.clone(bLanesA), bLanesA.getLanes().size() - lanes);
                }
                bLanesA.setType("forward");
                lanesA(bLanesA);
                bLanesC.setType("backward");
                lanesC(bLanesC);
            }
        }
        eventSpinerA = true;
    }

    private void listenerB() {
        BLanes bLanes = presetsData.defaultLanes("both_ways", 1);
        if (jchbothwayB.isSelected()) {
            jpanelcontentTurns.add(jpnlturnsB, BorderLayout.CENTER);
            bLanesB = bLanes;
            lanesB(bLanesB);
        } else {
            bLanesB = presetsData.defaultLanes("both_ways", 0);
            lanesB(bLanesB);
            jpnlturnsB.setBorder(null);
            jpnlturnsB.removeAll();
            jpnlturnsB.revalidate();
            jpnlturnsB.repaint();
        }
    }

    private void listenerC() {
        int lanes = Integer.valueOf(spinnerC.getValue().toString());
        if (eventSpinerC) {
            if (Util.isRightHandTraffic()) {

                if (lanes >= bLanesC.getLanes().size()) {
                    bLanesC = presetsData.addLanes((BLanes) Util.clone(bLanesC), "forward", lanes - bLanesC.getLanes().size());
                } else {
                    bLanesC = presetsData.removeLanes((BLanes) Util.clone(bLanesC), bLanesC.getLanes().size() - lanes);
                }
                bLanesC.setType("forward");
                lanesC(bLanesC);
                bLanesA.setType("backward");
                lanesA(bLanesA);

            } else {
                if (lanes >= bLanesC.getLanes().size()) {
                    bLanesC = presetsData.addLanes((BLanes) Util.clone(bLanesC), "forward", lanes - bLanesC.getLanes().size());
                } else {
                    bLanesC = presetsData.removeLanes((BLanes) Util.clone(bLanesC), bLanesC.getLanes().size() - lanes);
                }
                bLanesC.setType("backward");
                lanesC(bLanesC);
                bLanesA.setType("forward");
                lanesA(bLanesA);
            }
        }
        eventSpinerC = true;
    }

}
