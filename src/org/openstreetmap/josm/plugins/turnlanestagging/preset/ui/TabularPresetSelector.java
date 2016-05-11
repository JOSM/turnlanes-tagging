package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author ruben
 */
public class TabularPresetSelector extends JPanel {

    JSplitPane jSplitPane = null;
    JPanel jPanelBuldidTags = null;

    private JScrollPane scrollPane = null;
    private PresetsTable presetsTable = null;
    PresetsTableModel presetsTableModel;
    ArrayList<PresetTurnLane> listpresetturnlanes = new ArrayList<>();
    //panel for create the tags
    JComboBox<Integer> comboBox;
    JPanel panelGraps;

    public TabularPresetSelector() {
        build();
    }

    protected JScrollPane buildPresetGrid() {
        //add data        
        PresetTurnLane presetTurnLane = new PresetTurnLane("1", "turn:lanes 1", "left|through|right");
        PresetTurnLane presetTurnLane2 = new PresetTurnLane("2", "turn:lanes 2", "left|left|||right");
        listpresetturnlanes.add(presetTurnLane);
        listpresetturnlanes.add(presetTurnLane2);

        presetsTableModel = new PresetsTableModel(listpresetturnlanes);

        presetsTable = new PresetsTable(presetsTableModel);

        scrollPane = new JScrollPane(presetsTable);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setSize(new Dimension(50, 150));

        scrollPane.addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        Dimension d = scrollPane.getViewport().getExtentSize();
                        presetsTable.adjustColumnWidth(d.width);
//                        presetsTable.adjustColumnWidth(d.height - 200);

                    }
                }
        );
        // add the  click listener
        presetsTable.addMouseListener(new ClickAdapter());
        return scrollPane;
    }

    private class ClickAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                int rowNum = presetsTable.rowAtPoint(e.getPoint());
                System.err.println(getModel().listpresetturnlanes.get(rowNum).getTags());
            }
        }
    }

    protected PresetsTableModel getModel() {
        return (PresetsTableModel) presetsTable.getModel();
    }

    //form the tag
    protected JPanel buildPanelGrap() {
        //add data 

        jPanelBuldidTags = new JPanel(new BorderLayout());

        JPanel jpScroll = new JPanel(new GridLayout(1, 2));
        comboBox = new JComboBox<>();
        comboBox.addItem(1);
        comboBox.addItem(2);
        comboBox.addItem(3);
        comboBox.addItem(4);
        comboBox.addItem(5);
        comboBox.addItem(6);
        jpScroll.add(new JLabel("Number of Lines"));
        jpScroll.add(comboBox);

        panelGraps = new JPanel();

        jPanelBuldidTags.add(jpScroll, BorderLayout.NORTH);
        jPanelBuldidTags.add(panelGraps, BorderLayout.CENTER);
        jPanelBuldidTags.add(new JLabel("Buils tag Here"), BorderLayout.SOUTH);
        return jPanelBuldidTags;
    }

    public class Select extends ActionEvent {

        public Select(Object source, int id, String command) {
            super(source, id, command);
        }

    }

    protected void build() {
        setLayout(new BorderLayout());
        add(new JLabel("Select Preset Turn Lanes"), BorderLayout.NORTH);
        add(buildPresetGrid(), BorderLayout.CENTER);
        add(buildPanelGrap(), BorderLayout.SOUTH);

    }

}
