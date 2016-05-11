package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author ruben
 */
public class TabularPresetSelector extends JPanel {

    private JScrollPane scrollPane = null;
    private PresetsTable presetsTable = null;
    PresetsTableModel presetsTableModel;
    ArrayList<PresetTurnLane> listpresetturnlanes = new ArrayList<>();

    public TabularPresetSelector() {
        build();
    }

    protected JScrollPane buildPresetGrid() {
        //add data        
        PresetTurnLane presetTurnLane = new PresetTurnLane("1", "clasic", "|left|right");
        PresetTurnLane presetTurnLane2 = new PresetTurnLane("2", "New", "right|left|right");
        listpresetturnlanes.add(presetTurnLane);
        listpresetturnlanes.add(presetTurnLane2);
        
        presetsTableModel = new PresetsTableModel(listpresetturnlanes);
        presetsTable = new PresetsTable(presetsTableModel);
        scrollPane = new JScrollPane(presetsTable);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        Dimension d = scrollPane.getViewport().getExtentSize();
                        presetsTable.adjustColumnWidth(d.width);
                    }
                }
        );

        // add the  click listener
        presetsTable.addMouseListener(new ClickAdapter());
        return scrollPane;
    }

    protected void build() {
        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(buildPresetGrid());
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
}
