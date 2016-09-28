// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.preset;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.openstreetmap.josm.tools.ImageProvider;

/**
 *
 * @author ruben
 */
public class ImageRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setOpaque(true);
        setBackground(isSelected ? table.getSelectionBackground() : Color.white);
        setForeground(isSelected ? table.getForeground() : Color.black);
        if (value != null) {
            setText(value.toString());
            if (value.toString().contains("forward") || value.toString().contains("backward")) {
                setIcon(ImageProvider.get("table", "bidirectional.png"));
            } else {
                setIcon(ImageProvider.get("table", "unidirectional.png"));
            }
        }
        return this;
    }

}
