package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.bidirectional;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openstreetmap.josm.gui.oauth.AbstractAuthorizationUI;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.TurnSelection;
import org.openstreetmap.josm.plugins.turnlanestagging.util.Util;

/**
 *
 * @author ruben
 */
public class TurnSelectionBidirectional extends JPanel {

    JPanel jpanelcontent = null;

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
        add(buildselect());
    }

    public JPanel buildselect() {
        jpanelcontent = new JPanel(new GridLayout(1, 3, 50, 10));
        // A
        jpanelcontentA = new JPanel(new GridLayout(2, 1));
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
//        jpanelcontentA.add(jpnContentSpinnerA);
        jpanelcontentA.add(jpnlturnsA);

        //B
        jpanelcontentB = new JPanel(new GridLayout(2, 1));
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
//        jpanelcontentB.add(jpnContentSpinnerB);
        jpanelcontentB.add(jpnlturnsB);

        // C
        jpanelcontentC = new JPanel(new GridLayout(2, 1));
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
//        jpanelcontentC.add(jpnContentSpinnerC);
        jpanelcontentC.add(jpnlturnsC);

        //Add ALL Panels
        jpanelcontent.add(jpanelcontentA);
        jpanelcontent.add(jpanelcontentB);
        jpanelcontent.add(jpanelcontentC);

        return jpanelcontent;

    }

    class SPinnerListenerA implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            Util.print("value changed: " + spinnerA.getValue());
            lanesA("back", Integer.valueOf(spinnerA.getValue().toString()));
        }
    }

    class SPinnerListenerB implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            lanesB("back", Integer.valueOf(spinnerB.getValue().toString()));
        }
    }

    class SPinnerListenerC implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            lanesC("back", Integer.valueOf(spinnerC.getValue().toString()));
        }
    }

    ActionListener actionListenerA = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            AbstractButton aButton = (AbstractButton) actionEvent.getSource();
            System.out.println("Selected A: " + aButton.getText());
        }
    };

    ActionListener actionListenerB = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            AbstractButton aButton = (AbstractButton) actionEvent.getSource();
            System.out.println("Selected A: " + aButton.getText());
        }
    };

    ActionListener actionListenerC = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            AbstractButton aButton = (AbstractButton) actionEvent.getSource();
            System.out.println("Selected C: " + aButton.getText());
        }
    };

    public void lanesA(String type, int numLanes) {
        jpnlturnsA.removeAll();
        jpnlturnsA.setLayout(new GridLayout(1, numLanes));
        for (int i = 0; i < numLanes; i++) {
            BLine bLine = new BLine((i + 1), "");
            TurnSelection turnSelection = new TurnSelection(bLine);
            jpnlturnsA.add(turnSelection);
        }
        jpnlturnsA.revalidate();
        jpnlturnsA.repaint();
    }

    public void lanesB(String type, int numLanes) {
        jpnlturnsB.removeAll();
        jpnlturnsB.setLayout(new GridLayout(1, numLanes));
        for (int i = 0; i < numLanes; i++) {
            BLine bLine = new BLine((i + 1), "");
            TurnSelection turnSelection = new TurnSelection(bLine);
            jpnlturnsB.add(turnSelection);
        }
        jpnlturnsB.revalidate();
        jpnlturnsB.repaint();
    }

    public void lanesC(String type, int numLanes) {
        jpnlturnsC.removeAll();
        jpnlturnsC.setLayout(new GridLayout(1, numLanes));
        for (int i = 0; i < numLanes; i++) {
            BLine bLine = new BLine((i + 1), "");
            TurnSelection turnSelection = new TurnSelection(bLine);
            jpnlturnsC.add(turnSelection);
        }
        jpnlturnsC.revalidate();
        jpnlturnsC.repaint();
    }

}
