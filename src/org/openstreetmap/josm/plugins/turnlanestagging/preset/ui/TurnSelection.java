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

/**
 *
 * @author ruben
 */
public class TurnSelection extends JPanel {

    public static final String jRBLeft_CHANGED = "jRBLeft Changed";
    private ButtonGroup turnLeftRightGroup;
    private JCheckBox jCBThrough;
    private JPanel jPTurnSelection;
    private JPanel jPOptions;
    private JRadioButton jRBLeft;
    private JRadioButton jRBRight;
    String turn;
    String title;

    public TurnSelection(String title) {
        super();
        this.title = title;
        init();
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getTurn() {
        return turn;
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
        jPTurnSelection.add(new JLabel(title));

        jRBLeft.setText("L");
        jRBLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                builturn();
            }
        });
        jRBLeft.addActionListener(new jRBLeftListener());

        jRBRight.setText("R");
        jRBRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                builturn();
            }
        });

        jCBThrough.setText("T");
        jCBThrough.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                builturn();
            }
        });

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
        System.err.println(title + " : " + t);
        setTurn(t);
    }

    private class jRBLeftListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            String text = getTurn();
            firePropertyChange(jRBLeft_CHANGED, null, text);
            setTurn(text);           
        }
    }
}
