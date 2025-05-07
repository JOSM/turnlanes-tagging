// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.UIManager;

import org.openstreetmap.josm.gui.tagging.TagModel;
import org.openstreetmap.josm.plugins.tageditor.editor.TagEditorModel;

/**
 * This is the table cell renderer for cells for the table of tags in the tag
 * editor dialog.
 */
public class TagTableCellRenderer extends org.openstreetmap.josm.plugins.tageditor.editor.TagTableCellRenderer {

    /**
     * renders the background color. The default color is white. It is set to
     * {@link org.openstreetmap.josm.plugins.tageditor.editor.TagTableCellRenderer#BG_COLOR_HIGHLIGHTED BG_COLOR_HIGHLIGHTED}.
     *
     * @param tagModel the tag model
     * @param model the tag editor model
     *
     * @see org.openstreetmap.josm.plugins.tageditor.editor.TagTableCellRenderer#BG_COLOR_HIGHLIGHTED if this cell displays the
     * tag which is suggested by the currently selected preset.
     */
    protected void renderColor(TagModel tagModel, TagEditorModel model, boolean isSelected, boolean isvalTag) {
        if (isvalTag) {
            setBackground(new Color(247, 246, 225));
        } else {
            if (isSelected) {
                setBackground(UIManager.getColor("Table.selectionBackground"));
                setForeground(UIManager.getColor("Table.selectionForeground"));
            } else {
                setBackground(UIManager.getColor("Table.background"));
                setForeground(UIManager.getColor("Table.foreground"));
            }
        }
        if (belongsToSelectedPreset(tagModel, model)) {
            setBackground(BG_COLOR_HIGHLIGHTED);
        }
    }

    /**
     * replies the cell renderer component for a specific cell
     *
     * @param table the table
     * @param value the value to be rendered
     * @param isSelected true, if the value is selected
     * @param hasFocus true, if the cell has focus
     * @param rowIndex the row index
     * @param vColIndex the column index
     *
     * @return the renderer component
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

        resetRenderer();
        TagModel tagModel = (TagModel) value;

        switch (vColIndex) {
            case 0:
                renderTagName(tagModel);
                break;
            case 1:
                renderTagValue(tagModel);
                break;
        }

        String[] val_tags = {"lanes:backward",
            "turn:lanes:backward",
            "lanes:both_ways",
            "turn:lanes:both_ways",
            "lanes:forward",
            "turn:lanes:forward",
            "lanes",
            "turn:lanes"};

        if (Arrays.asList(val_tags).indexOf(tagModel.getName()) > -1) {
            renderColor(tagModel, (TagEditorModel) table.getModel(), isSelected, true);
            if (hasFocus && isSelected) {
                if (table.getSelectedColumnCount() == 1 && table.getSelectedRowCount() == 1) {
                    if (table.getEditorComponent() != null) {
                        table.getEditorComponent().requestFocusInWindow();
                    }
                }
            }
        } else {
            renderColor(tagModel, (TagEditorModel) table.getModel(), isSelected, false);
            if (hasFocus && isSelected) {
                if (table.getSelectedColumnCount() == 1 && table.getSelectedRowCount() == 1) {
                    if (table.getEditorComponent() != null) {
                        table.getEditorComponent().requestFocusInWindow();
                    }
                }
            }
        }

        return this;
    }
}
