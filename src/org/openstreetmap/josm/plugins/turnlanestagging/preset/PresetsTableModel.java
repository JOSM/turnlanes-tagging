// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.preset;

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
                if (listBRoad.get(rowIndex).getName().equals("Unidirectional")) {
                    return String.valueOf(listBRoad.get(rowIndex).getNumLanes());
                } else {
                    return String.valueOf(listBRoad.get(rowIndex).getNumLanesBidirectional());
                }
            case 2:
                if (listBRoad.get(rowIndex).getName().equals("Unidirectional")) {
                    return listBRoad.get(rowIndex).getTagturns();
                } else {
                    String birecTurns = "";
                    if (listBRoad.get(rowIndex).getLanesA().getLanes().size() > 0) {
                        birecTurns = listBRoad.get(rowIndex).getLanesA().getType() + ": " + listBRoad.get(rowIndex).getLanesA().getTagturns();
                    }
                    if (listBRoad.get(rowIndex).getLanesB().getLanes().size() > 0) {
                        birecTurns = birecTurns + "; " + listBRoad.get(rowIndex).getLanesB().getType() + ": " + listBRoad.get(rowIndex).getLanesB().getTagturns();
                    }
                    if (listBRoad.get(rowIndex).getLanesC().getLanes().size() > 0) {
                        birecTurns = birecTurns + "; " + listBRoad.get(rowIndex).getLanesC().getType() + ": " + listBRoad.get(rowIndex).getLanesC().getTagturns();
                    }
                    return birecTurns;
                }

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
