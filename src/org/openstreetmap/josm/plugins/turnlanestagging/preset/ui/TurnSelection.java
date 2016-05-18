package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 *
 * @author ruben
 */
public class TurnSelection extends JPanel {

    public static final String jRBLeft_CHANGED = "jRBLeft Changed";
    public static final String jRBRight_CHANGED = "jRBRight Changed";
    public static final String jCBThrough_CHANGED = "jCBThrough Changed";
    private JCheckBox jCBThrough;
    private JPanel jPTurnSelection;
    private JPanel jPOptions;
    private JCheckBox jCBLeft;
    private JCheckBox jCBRight;
    BLine bLine;

    public TurnSelection(BLine bl) {
        super();
        this.bLine = bl;
        init();
    }

    public void init() {
        jPTurnSelection = new JPanel();
        jPOptions = new JPanel();
        jCBLeft = new JCheckBox();
        jCBRight = new JCheckBox();
        jCBThrough = new JCheckBox();
        jPTurnSelection.setLayout(new GridLayout(2, 1));
        jPTurnSelection.setBorder(BorderFactory.createEtchedBorder());
        jPTurnSelection.add(new JLabel("Lane " + bLine.getPosition()));

//        jCBLeft.setText("L");
        jCBLeft.setIcon(ImageProvider.get("types", "empty.png"));
        jCBLeft.setSelectedIcon(ImageProvider.get("types", "left.png"));
        jCBLeft.addActionListener(new LeftListener());

//        jCBRight.setText("R");
        jCBRight.setIcon(ImageProvider.get("types", "empty.png"));
        jCBRight.setSelectedIcon(ImageProvider.get("types", "right.png"));
        jCBRight.addActionListener(new RightListener());

//        jCBThrough.setText("T");
        jCBThrough.setIcon(ImageProvider.get("types", "empty.png"));
        jCBThrough.setSelectedIcon(ImageProvider.get("types", "through.png"));
        jCBThrough.addActionListener(new ThroughListener());

        jPOptions = new JPanel(new GridLayout(1, 3));
        jPOptions.add(jCBLeft);
        jPOptions.add(jCBThrough);
        jPOptions.add(jCBRight);
        jPTurnSelection.add(jPOptions);
        setTurn();
        //add on Main Panel
        add(jPTurnSelection);
    }

    //left|left;through||…|right
    protected void builturn() {
        String t = "";
        if (jCBThrough.isSelected()) {
            t = "through";
        }
        if (jCBLeft.isSelected()) {
            t = "left";
        }
        if (jCBRight.isSelected()) {
            t = "right";
        }
        if (jCBThrough.isSelected() && jCBRight.isSelected()) {
            t = "through;right";
        }
        if (jCBLeft.isSelected() && jCBThrough.isSelected()) {
            t = "left;through";
        }
        if (jCBLeft.isSelected() && jCBRight.isSelected()) {
            t = "left;right";
        }
        if (jCBLeft.isSelected() && jCBThrough.isSelected() && jCBRight.isSelected()) {
            t = "left;through;right";
        }

        bLine.setTurn(t);
    }

    protected void setTurn() {
        if (bLine.getTurn().equals("left")) {
            jCBLeft.setSelected(true);
        }
        if (bLine.getTurn().equals("right")) {
            jCBRight.setSelected(true);
        }
        if (bLine.getTurn().equals("through")) {
            jCBThrough.setSelected(true);
        }
        if (bLine.getTurn().equals("through;right")) {
            jCBRight.setSelected(true);
            jCBThrough.setSelected(true);
        }
        if (bLine.getTurn().equals("left;through")) {
            jCBLeft.setSelected(true);
            jCBThrough.setSelected(true);
        }
        if (bLine.getTurn().equals("left;right")) {
            jCBLeft.setSelected(true);
            jCBRight.setSelected(true);
        }
        if (bLine.getTurn().equals("left;through;right")) {
            jCBLeft.setSelected(true);
            jCBThrough.setSelected(true);
            jCBRight.setSelected(true);
        }
    }

    private class LeftListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(jRBLeft_CHANGED, null, bLine);
        }
    }

    private class RightListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(jRBRight_CHANGED, null, bLine);
        }
    }

    private class ThroughListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(jCBThrough_CHANGED, null, bLine);
        }
    }
}
