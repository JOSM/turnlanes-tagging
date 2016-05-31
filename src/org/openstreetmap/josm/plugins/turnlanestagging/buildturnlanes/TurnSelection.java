package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
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
//    private JPanel jPTurnSelection;
    private JPanel jPOptions;
    private JCheckBox jCBLeft;
    private JCheckBox jCBRight;
    BLane bLine;

    public TurnSelection(BLane bl) {
        super();
        this.bLine = bl;
        init();
    }

    public void init() {
        jPOptions = new JPanel();
        jCBLeft = new JCheckBox();
        jCBRight = new JCheckBox();
        jCBThrough = new JCheckBox();

        jCBLeft.setIcon(ImageProvider.get("types", "empty.png"));

        jCBLeft.setSelectedIcon(ImageProvider.get("types", "left-" + bLine.getType() + ".png"));
        jCBLeft.addActionListener(new LeftListener());

        jCBRight.setIcon(ImageProvider.get("types", "empty.png"));
        jCBRight.setSelectedIcon(ImageProvider.get("types", "right-" + bLine.getType() + ".png"));
        jCBRight.addActionListener(new RightListener());

        jCBThrough.setIcon(ImageProvider.get("types", "empty.png"));
        jCBThrough.setSelectedIcon(ImageProvider.get("types", "through-" + bLine.getType() + ".png"));
        jCBThrough.addActionListener(new ThroughListener());

        jPOptions = new JPanel(new GridLayout(1, 3));
        jPOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lane " + bLine.getPosition(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));

        jPOptions.add(jCBLeft);
        jPOptions.add(jCBThrough);
        jPOptions.add(jCBRight);

        setTurn();
        //add on Main Panel
        add(jPOptions);
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
        if (bLine.getTurn().equals("through;right") || bLine.getTurn().equals("right;through")) {
            jCBRight.setSelected(true);
            jCBThrough.setSelected(true);
        }
        if (bLine.getTurn().equals("left;through") || bLine.getTurn().equals("through;left")) {
            jCBLeft.setSelected(true);
            jCBThrough.setSelected(true);
        }
        if (bLine.getTurn().equals("left;right") || bLine.getTurn().equals("right;left")) {
            jCBLeft.setSelected(true);
            jCBRight.setSelected(true);
        }
        if (bLine.getTurn().equals("left;through;right") || bLine.getTurn().equals("left;right;through")
                || bLine.getTurn().equals("through;right;left") || bLine.getTurn().equals("through;left;right")
                || bLine.getTurn().equals("right;left;through") || bLine.getTurn().equals("right;through;left")) {

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
