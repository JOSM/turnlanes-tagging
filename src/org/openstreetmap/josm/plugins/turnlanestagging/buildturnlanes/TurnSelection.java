package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
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
    public static final String Slight_right_CHANGED = "slight_right Changed";
    public static final String Slight_left_CHANGED = "slight_left Changed";
    public static final String Merge_to_right_CHANGED = "merge_to_right Changed";
    public static final String Merge_to_left_CHANGED = "merge_to_left Changed";

//    private JPanel jPTurnSelection;
    private JPanel jPOptions;
    private JCheckBox left;
    private JCheckBox through;
    private JCheckBox right;
    private JCheckBox slight_right;
    private JCheckBox slight_left;
    private JCheckBox merge_to_right;
    private JCheckBox merge_to_left;
    BLane bLine;
    int numRoadLanes;

    GridBagConstraints gbc = new GridBagConstraints();

    public TurnSelection(BLane bl, int numRoadLanes) {
        super();
        this.bLine = bl;
        this.numRoadLanes = numRoadLanes;
        init();
    }

    public void init() {
        jPOptions = new JPanel(new GridBagLayout());
        left = new JCheckBox();
        through = new JCheckBox();
        right = new JCheckBox();
        slight_right = new JCheckBox();
        slight_left = new JCheckBox();
        merge_to_right = new JCheckBox();
        merge_to_left = new JCheckBox();

        //Unidirectional
        if (bLine.getPosition() == 1 || bLine.getPosition() == numRoadLanes && (bLine.getType().equals("unid"))) {
            forwardFirstLast();
        } else {
            forwareMidle();
        }
        //Bidirectional
        if (bLine.getPosition() == 1 || bLine.getPosition() == numRoadLanes && (bLine.getType().equals("forward") || bLine.getType().equals("backward"))) {
            if (bLine.getType().equals("forward")) {
                forwardFirstLast();
            }
            if (bLine.getType().equals("backward")) {
                backwardFirstLast();
            }
        } else {
            if (bLine.getType().equals("forward")) {
                forwareMidle();
            }
            if (bLine.getType().equals("backward")) {
                backwardMidle();
            }
        }

        if (bLine.getType().equals("both_ways")) {
            both_waysOne();
        }

        left.addActionListener(new LeftListener());
        right.addActionListener(new RightListener());
        through.addActionListener(new ThroughListener());
        slight_right.addActionListener(new Slight_rightListener());
        slight_left.addActionListener(new Slight_leftListener());
        merge_to_right.addActionListener(new Merge_to_rightListener());
        merge_to_left.addActionListener(new Merge_to_leftListener());

        jPOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lane " + bLine.getPosition(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(102, 102, 102)));
        setTurn();
        add(jPOptions);
    }

    //left|left;through||…|right
    protected void builturn() {
        List<String> list = new ArrayList<>();
        //"reverse", "sharp_left", "left", "slight_left", "merge_to_right", "through", "merge_to_left", "slight_right", "right", "sharp_right"
        boolean status_left = left.isSelected();
        boolean status_through = through.isSelected();
        boolean status_right = right.isSelected();
        boolean status_slight_right = slight_right.isSelected();
        boolean status_slight_left = slight_left.isSelected();
        boolean status_merge_to_right = merge_to_right.isSelected();
        boolean status_merge_to_left = merge_to_left.isSelected();

        if (status_left) {
            list.add("left");
        }

        if (status_slight_left) {
            list.add("slight_left");
        }

        if (status_merge_to_right) {
            list.add("merge_to_right");
        }

        if (status_through) {
            list.add("through");
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
        String dirs[] = bLine.getTurn().split("\\;", -1);

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
                through.setSelected(true);
            }
        }
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

    public void forwardFirstLast() {
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
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //merge_to_left
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 2;
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
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

//        //merge_to_left
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridheight = 1;
//        jPOptions.add(merge_to_left, gbc);
//        merge_to_left.setVisible(false);
//
//        //merge_to_right
//        gbc.gridx = 2;
//        gbc.gridy = 2;
//        gbc.gridheight = 1;
//        jPOptions.add(merge_to_right, gbc);
//        merge_to_right.setVisible(false);
    }

    public void backwardFirstLast() {

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
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_left, gbc);

        //merge_to_right
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        jPOptions.add(merge_to_right, gbc);

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 2;
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
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

        //slight_left
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(slight_left, gbc);

        //slight_right
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        jPOptions.add(slight_right, gbc);

    }

    public void both_waysOne() {

        left.setIcon(ImageProvider.get("types", "left-forward-off.png"));
        through.setIcon(ImageProvider.get("types", "reverse-both_ways-off.png"));
        right.setIcon(ImageProvider.get("types", "right-forward-off.png"));

        left.setSelectedIcon(ImageProvider.get("types", "left-forward.png"));
        through.setSelectedIcon(ImageProvider.get("types", "reverse-both_ways.png"));
        right.setSelectedIcon(ImageProvider.get("types", "right-forward.png"));

        //left
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(left, gbc);

        //Through
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        jPOptions.add(through, gbc);

        //Right
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        jPOptions.add(right, gbc);

    }
}
