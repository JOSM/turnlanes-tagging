package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author ruben
 */
public class TurnLanesOptions extends JPanel {

    private ButtonGroup buttonGroup;
    private JCheckBox jCBThrough;
    private JPanel jPTurnLanesOptions;
    private JPanel jPTitle;
    private JPanel jPOptions;
    private JRadioButton jRBLeft;
    private JRadioButton jRBRight;

    public TurnLanesOptions(String title) {
        init(title);
    }

    public void init(String title) {
        buttonGroup = new ButtonGroup();
        jPTurnLanesOptions = new JPanel();
        jPOptions = new JPanel();
        jPTitle = new JPanel();
        jRBLeft = new JRadioButton();
        jRBRight = new JRadioButton();
        jCBThrough = new JCheckBox();
        jPTurnLanesOptions.setLayout(new GridLayout(2, 1));
        jPTurnLanesOptions.setBorder(BorderFactory.createEtchedBorder());
        jPTurnLanesOptions.add(new JLabel(title));

        jRBLeft.setText("L");
        jRBLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            }
        });

        jRBRight.setText("R");
        jRBRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            }
        });

        jCBThrough.setText("T");
        jCBThrough.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            }
        });

        buttonGroup.add(jRBLeft);
        buttonGroup.add(jRBRight);

        jPOptions = new JPanel(new GridLayout(1, 3));
        jPOptions.add(jRBLeft);
        jPOptions.add(jCBThrough);
        jPOptions.add(jRBRight);

        jPTurnLanesOptions.add(jPOptions);
        add(jPTurnLanesOptions);
       

    }

}
