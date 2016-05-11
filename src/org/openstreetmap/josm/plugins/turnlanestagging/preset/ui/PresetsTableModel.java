// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class PresetsTableModel extends AbstractTableModel {

    ArrayList<PresetTurnLane> listpresetturnlanes;
    Class[] columns = {Object.class, Object.class};
    String titles[] = {"Name", "tags"};

    public PresetsTableModel(ArrayList<PresetTurnLane> list) {
        super();
        this.listpresetturnlanes = list;
    }

    @Override
    public int getRowCount() {
        return listpresetturnlanes.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        System.err.println(columnIndex);
        switch (columnIndex) {
            case 0:
                return listpresetturnlanes.get(rowIndex).getName();
            case 1:
                return listpresetturnlanes.get(rowIndex).getTags();
            default:
                return null;
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return columns[columnIndex];

    }

    @Override
    public String getColumnName(int columnIndex) {
        return titles[columnIndex];

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
