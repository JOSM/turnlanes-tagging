package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author ruben
 */
public class TabularPresetSelector extends JPanel {

    private JScrollPane scrollPane = null;
//    private PresetsTable presetsTable = null;
    PresetsTableModel presetsTableModel;
    private JTable jTable;
    ArrayList<PresetTurnLane> listpresetturnlanes = new ArrayList<>();

    ;

    public TabularPresetSelector() {
        build();
    }

    protected JScrollPane buildPresetGrid() {
        //add data        
        PresetTurnLane presetTurnLane = new PresetTurnLane("1", "clasic", "|lest|right");
        listpresetturnlanes.add(presetTurnLane);

        presetsTableModel = new PresetsTableModel(listpresetturnlanes);

        jTable = new JTable(presetsTableModel);
        scrollPane = new JScrollPane(jTable);

//        presetsTable.setPreferredScrollableViewportSize(new Dimension(450, 63));
//        presetsTable.setFillsViewportHeight(true);
//        presetsTable.setVisible(true);
//
//        scrollPane = new JScrollPane(presetsTable);
//        scrollPane.setVisible(true);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

         scrollPane.setSize(new Dimension(600, 300));
        return scrollPane;
    }

    protected void build() {
        setLayout(new BorderLayout());
        add(buildPresetGrid(), BorderLayout.CENTER);

    }

//    protected PresetsTableModel getModel() {
//        return (PresetsTableModel) presetsTable.getModel();
//    }
}
