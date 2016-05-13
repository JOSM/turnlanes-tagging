package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;

/**
 *
 * @author ruben
 */
public class TurnSelection extends JPanel {

    public static final String jRBLeft_CHANGED = "jRBLeft Changed";
    public static final String jRBRight_CHANGED = "jRBRight Changed";
    public static final String jCBThrough_CHANGED = "jCBThrough Changed";

    private ButtonGroup turnLeftRightGroup;
    private JCheckBox jCBThrough;
    private JPanel jPTurnSelection;
    private JPanel jPOptions;
    private JRadioButton jRBLeft;
    private JRadioButton jRBRight;
    BLine bLine;

    public TurnSelection(int position, String turn) {
        super();
        bLine = new BLine();
        bLine.setPosition(position);
        bLine.setTurn(turn);
        init();
    }

    public void init() {
        turnLeftRightGroup = new ButtonGroup();
        jPTurnSelection = new JPanel();
        jPOptions = new JPanel();
        jRBLeft = new JRadioButton();
        jRBRight = new JRadioButton();
        jCBThrough = new JCheckBox();
        jPTurnSelection.setLayout(new GridLayout(2, 1));
        jPTurnSelection.setBorder(BorderFactory.createEtchedBorder());
        jPTurnSelection.add(new JLabel("Line" + bLine.getPosition()));

        jRBLeft.setText("L");
//        jRBLeft.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                builturn();
//            }
//        });
        jRBLeft.addActionListener(new LeftListener());

        jRBRight.setText("R");
//        jRBRight.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                builturn();
//            }
//        });
        jRBRight.addActionListener(new RightListener());

        jCBThrough.setText("T");
//        jCBThrough.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                builturn();
//            }
//        });

        jCBThrough.addActionListener(new ThroughListener());

        turnLeftRightGroup.add(jRBLeft);
        turnLeftRightGroup.add(jRBRight);
        jPOptions = new JPanel(new GridLayout(1, 3));
        jPOptions.add(jRBLeft);
        jPOptions.add(jCBThrough);
        jPOptions.add(jRBRight);
        jPTurnSelection.add(jPOptions);
        //add on Main Panel
        add(jPTurnSelection);
    }

    //left|left;through||…|right
    protected void builturn() {
        String t = "";
        if (jCBThrough.isSelected()) {
            t = "through";
        }
        if (jRBLeft.isSelected()) {
            t = "left";
        }
        if (jRBRight.isSelected()) {
            t = "right";
        }
        if (jRBRight.isSelected() && jCBThrough.isSelected()) {
            t = "right;through";
        }
        if (jRBLeft.isSelected() && jCBThrough.isSelected()) {
            t = "left;through";
        }
        bLine.setTurn(t);
    }

    private class LeftListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
//            String text = bgetTurn();
            firePropertyChange(jRBLeft_CHANGED, null, bLine);
//            setTurn(text);
        }
    }

    private class RightListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
//            String text = getTurn();
            firePropertyChange(jRBRight_CHANGED, null, bLine);
//            setTurn(text);
        }
    }

    private class ThroughListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
//            String text = getTurn();
            firePropertyChange(jCBThrough_CHANGED, null, bLine);
//            setTurn(text);
        }
    }
}
