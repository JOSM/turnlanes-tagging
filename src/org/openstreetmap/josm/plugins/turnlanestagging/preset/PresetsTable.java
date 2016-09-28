// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.preset;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

public class PresetsTable extends JTable {

    /**
     * initialize the table
     */
    protected void init() {
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(18);
    }

    public PresetsTable(PresetsTableModel model) {
        super(model);
        init();
    }

    public void adjustColumnWidth(int scrollPaneWidth) {
        TableColumnModel tcm = getColumnModel();
        int width = scrollPaneWidth;
        width = width / 3;
        int reWidth = width / 2;
        if (width > 0) {
            tcm.getColumn(0).setMinWidth(width - reWidth);
            tcm.getColumn(0).setMaxWidth(width - reWidth);
            tcm.getColumn(1).setMinWidth(width - reWidth);
            tcm.getColumn(1).setMaxWidth(width - reWidth);
            tcm.getColumn(2).setMinWidth(width + reWidth + reWidth);
            tcm.getColumn(2).setMaxWidth(width + reWidth + reWidth);
        }
    }
}
