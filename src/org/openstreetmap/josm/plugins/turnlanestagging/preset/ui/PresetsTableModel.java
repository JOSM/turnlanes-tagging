// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;

public class PresetsTableModel extends AbstractTableModel {

    List<BRoad> listBRoad;
    Class[] columns = {Object.class, Object.class, Object.class};
    String titles[] = {"Directional", "Number of lanes", "Turn lanes"};

    public PresetsTableModel(List<BRoad> list) {
        super();
        this.listBRoad = list;
    }

    @Override
    public int getRowCount() {
        return listBRoad.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return listBRoad.get(rowIndex).getName();
            case 1:
                return String.valueOf(listBRoad.get(rowIndex).getNumLanes());
            case 2:
                return listBRoad.get(rowIndex).getTagturns();
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

    public void clear() {
        listBRoad.clear();
    }
}
