// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 *
 * @author ruben
 */
public class TurnSelection extends JPanel {

    public static final String Left_CHANGED = "Left Changed";
    public static final String Right_CHANGED = "Right Changed";
    public static final String Through_CHANGED = "Through Changed";
    public static final String Reversible_CHANGED = "Reversible Changed";
    public static final String Slight_right_CHANGED = "slight_right Changed";
    public static final String Slight_left_CHANGED = "slight_left Changed";
    public static final String Merge_to_right_CHANGED = "merge_to_right Changed";
    public static final String Merge_to_left_CHANGED = "merge_to_left Changed";
    public static final String Reverse_CHANGED = "reverse Changed";
    public static final String CHANGE_CHANGED = "Change Changed";

    private static final String[] CHANGE_OPTIONS = {"", "yes", "no", "not_left", "not_right"};

//    private JPanel jPTurnSelection;
    private JPanel jPOptions;
    private JCheckBox left;
    private JCheckBox through;
    private JCheckBox reversible;
    private JCheckBox right;
    private JCheckBox slight_right;
    private JCheckBox slight_left;
    private JCheckBox merge_to_right;
    private JCheckBox merge_to_left;
    private JCheckBox reverse;
    private JComboBox<String> change;
    private JLabel lblChange;

    BLane bLine;
    int numRoadLanes;
    boolean isRightHand;

    GridBagConstraints gbc = new GridBagConstraints();

    public TurnSelection(BLane bl, int numRoadLanes, boolean isRightHand) {
        super();
        this.bLine = bl;
        this.numRoadLanes = numRoadLanes;
        this.isRightHand = isRightHand;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        jPOptions = new JPanel(new GridBagLayout());
        left = new JCheckBox();
        through = new JCheckBox();
        reversible = new JCheckBox();
        right = new JCheckBox();
        slight_right = new JCheckBox();
        slight_left = new JCheckBox();
        merge_to_right = new JCheckBox();
        merge_to_left = new JCheckBox();
        reverse = new JCheckBox();
        change = new JComboBox<>(CHANGE_OPTIONS);

        //ToolTipText
        left.setToolTipText("left");
        through.setToolTipText("through");
        reversible.setToolTipText("reversible");
        right.setToolTipText("right");
        slight_right.setToolTipText("slight_right");
        slight_left.setToolTipText("slight_left");
        merge_to_right.setToolTipText("merge_to_right");
        merge_to_left.setToolTipText("merge_to_left");
        reverse.setToolTipText("reverse");
        change.setToolTipText("change");

        //Unidirectional
        if (bLine.getType().equals("unid")) {
            if (bLine.getPosition() == 1) {
                unidFirst();
            } else if (bLine.getPosition() == numRoadLanes) {
                unidLast();
            } else {
                unidMidle();
            }
        }

        //Bidirectional
        if (bLine.getType().equals("forward") || bLine.getType().equals("backward")) {
            if (bLine.getPosition() == 1) {
                if (bLine.getType().equals("forward")) {
                    forwardFirst();
                }
                if (bLine.getType().equals("backward")) {
                    backwardFirst();
                }
            } else if (bLine.getPosition() == numRoadLanes) {
                if (bLine.getType().equals("forward")) {
                    forwardLast();
                }
                if (bLine.getType().equals("backward")) {
                    backwardLast();
                }

            } else {
                if (bLine.getType().equals("forward")) {
                    forwareMidle();
                }
                if (bLine.getType().equals("backward")) {
                    backwardMidle();
                }
            }

        }

        if (bLine.getType().equals("both_ways")) {
            both_waysOne();
        }

        left.addActionListener(new LeftListener());
        right.addActionListener(new RightListener());
        through.addActionListener(new ThroughListener());
        reversible.addActionListener(new ReversibleListener());
        slight_right.addActionListener(new Slight_rightListener());
        slight_left.addActionListener(new Slight_leftListener());
        merge_to_right.addActionListener(new Merge_to_rightListener());
        merge_to_left.addActionListener(new Merge_to_leftListener());
        reverse.addActionListener(new ReverseListener());
        change.addActionListener(new ChangeListener());

        jPOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lane " + bLine.getPosition(),
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Abyssinica SIL", 0, 11), Color.gray));
        lblChange = new JLabel(" ", JLabel.CENTER);
        lblChange.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        setTurn();
        setChange();
        // Tint the whole panel by travel direction, like the old road preview did.
        Color tint = directionColor(bLine.getType());
        setOpaque(true);
        setBackground(tint);
        jPOptions.setBackground(tint);
        for (Component comp : jPOptions.getComponents()) {
            if (comp instanceof JComponent) {
                ((JComponent) comp).setOpaque(false);
            }
        }
        add(buildChangePanel(tint), BorderLayout.NORTH);
        add(jPOptions, BorderLayout.CENTER);
        add(lblChange, BorderLayout.SOUTH);
        updateChangeLabel();
    }

    private JPanel buildChangePanel(Color tint) {
        JPanel pnl = new JPanel(new BorderLayout(5, 0));
        pnl.setBackground(tint);
        pnl.add(new JLabel("Change"), BorderLayout.WEST);
        pnl.add(change, BorderLayout.CENTER);
        return pnl;
    }

    /**
     * Updates the coloured label showing the lane's {@code change:lanes} value.
     */
    private void updateChangeLabel() {
        if (lblChange == null) {
            return;
        }
        String c = bLine.getChange();
        if (c == null || c.isEmpty()) {
            lblChange.setText(" ");
        } else {
            lblChange.setText("change: " + c);
            lblChange.setForeground(changeColor(c));
        }
    }

    /**
     * Background tint per travel direction: blue for forward/unidirectional
     * lanes, red for backward lanes and yellow for both-ways lanes.
     */
    private Color directionColor(String type) {
        if ("forward".equals(type) || "unid".equals(type)) {
            return new Color(190, 215, 255);
        } else if ("backward".equals(type)) {
            return new Color(255, 200, 190);
        } else if ("both_ways".equals(type)) {
            return new Color(255, 230, 160);
        }
        return new Color(238, 238, 238);
    }

    private Color changeColor(String change) {
        switch (change) {
            case "yes":
                return new Color(0, 128, 0);
            case "no":
                return new Color(200, 0, 0);
            case "not_left":
            case "not_right":
                return new Color(200, 120, 0);
            default:
                return Color.DARK_GRAY;
        }
    }

    //left|left;through||…|right
    protected void builturn() {
        List<String> list = new ArrayList<>();
        //"reverse", "sharp_left", "left", "slight_left", "merge_to_right", "through", "merge_to_left", "slight_right", "right", "sharp_right"
        boolean status_left = left.isSelected();
        boolean status_through = through.isSelected();
        boolean status_reversible = reversible.isSelected();
        boolean status_right = right.isSelected();
        boolean status_slight_right = slight_right.isSelected();
        boolean status_slight_left = slight_left.isSelected();
        boolean status_merge_to_right = merge_to_right.isSelected();
        boolean status_merge_to_left = merge_to_left.isSelected();
        boolean status_reverse = reverse.isSelected();

        if (status_reverse) {
            list.add("reverse");
        }

        if (status_left) {
            list.add("left");
        }

        if (status_slight_left) {
            list.add("slight_left");
        }

        if (status_merge_to_right) {
            list.add("merge_to_right");
        }

        if (status_through && (bLine.getType().equals("forward") || bLine.getType().equals("backward") || bLine.getType().equals("unid"))) {
            list.add("through");
        }

        if (status_reversible) {
            list.add("reversible");
        }

        if (status_merge_to_left) {
            list.add("merge_to_left");
        }

        if (status_slight_right) {
            list.add("slight_right");
        }

        if (status_right) {
            list.add("right");
        }

        String t = list.toString().replace("[", "").replace("]", "").replace(", ", ";");
        bLine.setTurn(t);

    }

    protected void setTurn() {
        String[] dirs = bLine.getTurn().split("\\;", -1);

        //"reverse", "sharp_left", "left", "slight_left", "merge_to_right", "through", "merge_to_left", "slight_right", "right", "sharp_right"
        for (int i = 0; i < dirs.length; i++) {

            if (dirs[i].equals("left")) {
                left.setSelected(true);
            }

            if (dirs[i].equals("slight_left")) {
                slight_left.setSelected(true);
            }

            if (dirs[i].equals("merge_to_right")) {
                merge_to_right.setSelected(true);
            }

            if (dirs[i].equals("through")) {
                through.setSelected(true);
            }

            if (dirs[i].equals("merge_to_left")) {
                merge_to_left.setSelected(true);
            }

            if (dirs[i].equals("slight_right")) {
                slight_right.setSelected(true);
            }

            if (dirs[i].equals("right")) {
                right.setSelected(true);
            }

            if (dirs[i].equals("reverse")) {
                reverse.setSelected(true);
            }

            if (dirs[i].equals("reversible")) {
                reversible.setSelected(true);
            }
        }
    }

    protected void setChange() {
        change.setSelectedItem(bLine.getChange());
        updateChangeLabel();
    }

    private class LeftListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Left_CHANGED, null, bLine);
        }
    }

    private class RightListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Right_CHANGED, null, bLine);
        }
    }

    private class ThroughListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Through_CHANGED, null, bLine);
        }
    }

    private class ReversibleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Reversible_CHANGED, null, bLine);
        }
    }

    private class Slight_rightListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Slight_right_CHANGED, null, bLine);
        }
    }

    private class Slight_leftListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Slight_left_CHANGED, null, bLine);
        }
    }

    private class Merge_to_rightListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Merge_to_right_CHANGED, null, bLine);
        }
    }

    private class Merge_to_leftListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Merge_to_left_CHANGED, null, bLine);
        }
    }

    private class ReverseListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            builturn();
            firePropertyChange(Reverse_CHANGED, null, bLine);
        }
    }

    private class ChangeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Object selected = change.getSelectedItem();
            bLine.setChange(selected == null ? "" : selected.toString());
            updateChangeLabel();
            firePropertyChange(CHANGE_CHANGED, null, bLine);
        }
    }

    public void forwardFirst() {
        slight_left.setIcon(ImageProvider.get("types", "slight_left-forward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-forward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-forward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-forward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-forward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-forward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-forward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-forward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-forward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-forward.png"));

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //merge_to_left
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_right, gbc);

        if (isRightHand) {
            reverse.setIcon(ImageProvider.get("types", "reverse-righthand-forward-off.png"));
            reverse.setSelectedIcon(ImageProvider.get("types", "reverse-righthand-forward.png"));
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridheight = 1;
            jPOptions.add(reverse, gbc);
        } else {
            //reverse
            reverse.setIcon(ImageProvider.get("types", "reverse-lefthand-forward-off.png"));
            reverse.setSelectedIcon(ImageProvider.get("types", "reverse-lefthand-forward.png"));
            gbc.gridx = 2;
            gbc.gridy = 3;
            gbc.gridheight = 1;
            jPOptions.add(reverse, gbc);
        }

    }

    public void forwardLast() {
        slight_left.setIcon(ImageProvider.get("types", "slight_left-forward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-forward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-forward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-forward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-forward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-forward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-forward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-forward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-forward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-forward.png"));

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //merge_to_left
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_right, gbc);
    }

    public void forwareMidle() {
        slight_left.setIcon(ImageProvider.get("types", "slight_left-forward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-forward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-forward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-forward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-forward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-forward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-forward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-forward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-forward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-forward.png"));

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);
    }

    public void backwardFirst() {

        slight_left.setIcon(ImageProvider.get("types", "slight_left-backward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-backward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-backward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-backward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-backward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-backward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-backward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-backward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-backward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-backward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-backward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-backward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-backward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-backward.png"));

        if (isRightHand) {
            //reverse righthand
            reverse.setIcon(ImageProvider.get("types", "reverse-righthand-backward-off.png"));
            reverse.setSelectedIcon(ImageProvider.get("types", "reverse-righthand-backward.png"));
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            jPOptions.add(reverse, gbc);

        } else {
            //reverse lefthand
            reverse.setIcon(ImageProvider.get("types", "reverse-lefthand-backward-off.png"));
            reverse.setSelectedIcon(ImageProvider.get("types", "reverse-lefthand-backward.png"));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            jPOptions.add(reverse, gbc);
        }

        //merge_to_left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_right, gbc);

        //left
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //slight_left
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

    }

    public void backwardLast() {

        slight_left.setIcon(ImageProvider.get("types", "slight_left-backward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-backward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-backward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-backward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-backward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-backward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-backward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-backward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-backward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-backward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-backward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-backward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-backward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-backward.png"));

        //merge_to_left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_right, gbc);

        //left
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //slight_left
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

    }

    public void backwardMidle() {
        slight_left.setIcon(ImageProvider.get("types", "slight_left-backward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-backward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-backward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-backward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-backward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-backward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-backward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-backward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-backward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-backward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-backward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-backward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-backward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-backward.png"));

        //left
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //slight_left
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

    }

    public void both_waysOne() {

        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        reversible.setIcon(ImageProvider.get("types", "reversible-both_ways-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));

        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        reversible.setSelectedIcon(ImageProvider.get("types", "reversible-both_ways.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        jPOptions.add(reversible, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

    }

    public void unidFirst() {
        slight_left.setIcon(ImageProvider.get("types", "slight_left-forward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-forward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-forward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-forward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-forward-off.png"));
        reverse.setIcon(ImageProvider.get("types", "reverse-unid-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-forward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-forward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-forward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-forward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-forward.png"));
        reverse.setSelectedIcon(ImageProvider.get("types", "reverse-unid.png"));

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //merge_to_left
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_right, gbc);

        //reverse
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        jPOptions.add(reverse, gbc);

    }

    public void unidLast() {
        slight_left.setIcon(ImageProvider.get("types", "slight_left-forward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-forward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-forward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-forward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-forward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-forward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-forward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-forward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-forward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-forward.png"));

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //merge_to_left
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_right, gbc);
    }

    public void unidMidle() {
        slight_left.setIcon(ImageProvider.get("types", "slight_left-forward-off.png"));
        slight_right.setIcon(ImageProvider.get("types", "slight_right-forward-off.png"));
        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        through.setIcon(ImageProvider.get("types", "through-forward-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));
        merge_to_left.setIcon(ImageProvider.get("types", "merge_to_left-forward-off.png"));
        merge_to_right.setIcon(ImageProvider.get("types", "merge_to_right-forward-off.png"));

        slight_left.setSelectedIcon(ImageProvider.get("types", "slight_left-forward.png"));
        slight_right.setSelectedIcon(ImageProvider.get("types", "slight_right-forward.png"));
        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "through-forward.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));
        merge_to_left.setSelectedIcon(ImageProvider.get("types", "merge_to_left-forward.png"));
        merge_to_right.setSelectedIcon(ImageProvider.get("types", "merge_to_right-forward.png"));

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);
    }
}
