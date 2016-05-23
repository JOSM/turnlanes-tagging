package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.bidirectional;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author ruben
 */
public class TurnSelectionBidirectional extends JPanel {

    private JPanel jpnlLaneA = null;
    private ButtonGroup bgLaneA = null;
    private JRadioButton jrbLaneAForward = null;
    private JRadioButton jrbLaneABackward = null;
    private JRadioButton jrbLaneAboth_way = null;

    private JPanel jpnlLaneB = null;
    private ButtonGroup bgLaneB = null;
    private JRadioButton jrbLaneBForward = null;
    private JRadioButton jrbLaneBBackward = null;
    private JRadioButton jrbLaneBboth_way = null;

    private JPanel jpnlLaneC = null;
    private ButtonGroup bgLaneC = null;
    private JRadioButton jrbLaneCForward = null;
    private JRadioButton jrbLaneCBackward = null;
    private JRadioButton jrbLaneCboth_way = null;

    public TurnSelectionBidirectional() {
        super();
        init();
    }

    public void init() {
        //add on Main Panel
        add(buildselect());
    }

    public JPanel buildselect() {
        JPanel jpnlContent = new JPanel(new GridLayout(1, 3));
        //Forward
        jpnlLaneA = new JPanel(new GridLayout(3, 1));
        bgLaneA = new ButtonGroup();
        jrbLaneAForward = new JRadioButton("Forward");
        jrbLaneAboth_way = new JRadioButton("Both Way");
        jrbLaneABackward = new JRadioButton("Backward");
        bgLaneA.add(jrbLaneAForward);
        bgLaneA.add(jrbLaneAboth_way);
        bgLaneA.add(jrbLaneABackward);

        jpnlLaneA.add(jrbLaneAForward);
        jpnlLaneA.add(jrbLaneAboth_way);
        jpnlLaneA.add(jrbLaneABackward);

        jrbLaneAForward.addActionListener(actionListenerA);
        jrbLaneAboth_way.addActionListener(actionListenerA);
        jrbLaneABackward.addActionListener(actionListenerA);

        //both_way
        jpnlLaneB = new JPanel(new GridLayout(3, 1));
        bgLaneB = new ButtonGroup();
        jrbLaneBForward = new JRadioButton("Forward");
        jrbLaneBboth_way = new JRadioButton("Both Way");
        jrbLaneBBackward = new JRadioButton("Backward");

        bgLaneB.add(jrbLaneBForward);
        bgLaneB.add(jrbLaneBboth_way);
        bgLaneB.add(jrbLaneBBackward);

        jpnlLaneB.add(jrbLaneBForward);
        jpnlLaneB.add(jrbLaneBboth_way);
        jpnlLaneB.add(jrbLaneBBackward);

        jrbLaneBForward.addActionListener(actionListenerB);
        jrbLaneBboth_way.addActionListener(actionListenerB);
        jrbLaneBBackward.addActionListener(actionListenerB);

        //backward
        jpnlLaneC = new JPanel(new GridLayout(3, 1));
        bgLaneC = new ButtonGroup();
        jrbLaneCForward = new JRadioButton("Forward");
        jrbLaneCboth_way = new JRadioButton("Both Way");
        jrbLaneCBackward = new JRadioButton("Backward");

        bgLaneC.add(jrbLaneCForward);
        bgLaneC.add(jrbLaneCboth_way);
        bgLaneC.add(jrbLaneCBackward);

        jpnlLaneC.add(jrbLaneCForward);
        jpnlLaneC.add(jrbLaneCboth_way);
        jpnlLaneC.add(jrbLaneCBackward);

        jpnlContent.add(jpnlLaneA);
        jpnlContent.add(jpnlLaneB);
        jpnlContent.add(jpnlLaneC);
        return jpnlContent;

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
            System.out.println("Selected B: " + aButton.getText());
        }
    };
    
    

}
